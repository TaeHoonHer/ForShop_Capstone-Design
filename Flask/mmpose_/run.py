# Copyright (c) OpenMMLab. All rights reserved.
import mimetypes
import os
import time
from argparse import ArgumentParser

import cv2
import json_tricks as json
import mmcv
import mmengine
import numpy as np
import csv
import math
import torch
import pandas as pd

from scipy.spatial.distance import cosine
from mmpose.apis import inference_topdown
from mmpose.apis import init_model as init_pose_estimator
from mmpose.evaluation.functional import nms
from mmpose.registry import VISUALIZERS
from mmengine import Config, optim
from mmengine.registry import OPTIMIZERS
from mmpose.structures import merge_data_samples, split_instances
from mmpose.utils import adapt_mmdet_pipeline
from mmdet.apis import inference_detector, init_detector
try:
    from mmdet.apis import inference_detector, init_detector
    has_mmdet = True
except (ImportError, ModuleNotFoundError):
    has_mmdet = False

def process_one_image(args,
                      img,
                      detector,
                      pose_estimator,
                      visualizer=None,
                      show_interval=0):
    """Visualize predicted keypoints (and heatmaps) of one image."""

    # predict bbox
    det_result = inference_detector(detector, img)
    pred_instance = det_result.pred_instances.cpu().numpy()
    bboxes = np.concatenate(
        (pred_instance.bboxes, pred_instance.scores[:, None]), axis=1)
    bboxes = bboxes[np.logical_and(pred_instance.labels == args.det_cat_id,
                                   pred_instance.scores > args.bbox_thr)]
    bboxes = bboxes[nms(bboxes, args.nms_thr), :4]

    # predict keypoints
    pose_results = inference_topdown(pose_estimator, img, bboxes)
    data_samples = merge_data_samples(pose_results)

    # show the results
    if isinstance(img, str):
        img = mmcv.imread(img, channel_order='rgb')
    elif isinstance(img, np.ndarray):
        img = mmcv.bgr2rgb(img)
    
    black_img = np.zeros_like(img) # 흑백이미지로 만들기

    if visualizer is not None:
        visualizer.add_datasample(
            'result',
            img,
            # black_img,
            data_sample=data_samples,
            draw_gt=False,
            draw_heatmap=args.draw_heatmap,
            draw_bbox=args.draw_bbox,
            show_kpt_idx=args.show_kpt_idx,
            skeleton_style=args.skeleton_style,
            show=args.show,
            wait_time=show_interval,
            kpt_thr=args.kpt_thr)

    # if there is no instance detected, return None
    return data_samples.get('pred_instances', None)


def getAngle(a, b, c):
    ang = math.degrees(math.atan2(c[1]-b[1], c[0]-b[0]) - math.atan2(a[1]-b[1], a[0]-b[0]))
    ang = ang + 360 if ang < 0 else ang
    if ang > 180:
        ang = 360-ang    
    return ang

#이건 그저 좌표
def save_pred_instances_to_coordinate_csv(input_type, pred_instances, csv_file):
    with open(csv_file, mode='w', newline='') as file:
        writer = csv.writer(file)
        if input_type == 'image': # 이미지일 경우 csv 생성
                # 관절 라벨을 헤더로 작성
                num_keypoints = pred_instances['keypoints'].shape[1]
                # labels = ['frame']
                labels = []
                for i in range(num_keypoints):
                    labels.extend([f'joint{i+1}_x', f'joint{i+1}_y'])
                writer.writerow(labels)
                
                # 각 프레임의 관절 좌표를 작성
                for frame_id, keypoints in enumerate(pred_instances['keypoints'][0]): # 일단 현 단계에선 1개의 객체만 볼것
                    # 1단계만 볼 거면 사실 image쪽에도 pred_instances_csv list 넣어서 하면 이미지 영상 똑같이가능함.
                    # frame_data = [frame_id]  # 프레임 ID
                    frame_data=[]
                    # 관절 좌표 작성
                    for joint in keypoints:
                        frame_data.extend([joint[0], joint[1]])  # x 좌표, y 좌표
                        
                    writer.writerow(frame_data)

        elif input_type in ['webcam', 'video']: #영상일경우 csv 생성
                # 관절 라벨을 헤더로 작성
                num_keypoints = pred_instances[0].shape[0]
                # labels = ['frame']
                labels = []
                for i in range(num_keypoints):
                    labels.extend([f'joint{i+1}_x', f'joint{i+1}_y'])
                writer.writerow(labels)
                
                # 각 프레임의 관절 좌표를 작성
                for frame_id, keypoints in enumerate(pred_instances): # 일단 현 단계에선 1개의 객체만 볼것.
                    # frame_data = [frame_id]  # 프레임 ID
                    frame_data=[]
                    # 관절 좌표 작성
                    for joint in keypoints:
                        frame_data.extend([joint[0], joint[1]])  # x 좌표, y 좌표
                        
                    writer.writerow(frame_data)
                    
# 관절간의 각도 주요지점들.
def save_pred_instances_to_csv(input_type, pred_instances, csv_file):
    with open(csv_file, mode='w', newline='') as file:
        writer = csv.writer(file)
        if input_type == 'image': # 이미지일 경우 csv 생성
                # 관절 라벨을 헤더로 작성
                num_keypoints = pred_instances['keypoints'].shape[1]
                # labels = ['frame']
                labels = []
                for i in range(num_keypoints):
                    labels.extend([f'joint{i+1}_x', f'joint{i+1}_y'])
                writer.writerow(labels)
                
                # 각 프레임의 관절 좌표를 작성
                for frame_id, keypoints in enumerate(pred_instances['keypoints'][0]): # 일단 현 단계에선 1개의 객체만 볼것
                    # 1단계만 볼 거면 사실 image쪽에도 pred_instances_csv list 넣어서 하면 이미지 영상 똑같이가능함.
                    # frame_data = [frame_id]  # 프레임 ID
                    frame_data=[]
                    # 관절 좌표 작성
                    for joint in keypoints:
                        frame_data.extend([joint[0], joint[1]])  # x 좌표, y 좌표
                        
                    writer.writerow(frame_data)

        elif input_type in ['webcam', 'video']: #영상일경우 csv 생성
                #L손목 L팔꿈치 L어깨 L엉덩 L무릎 L발목 R손목 R팔꿈치 R어깨 R엉덩 R무릎 R발목 코
                labels = ["left_wrist", "left_elbow", "left_shoulder", "left_hip", "left_knee", "left_ankle", 
                           "right_wrist", "right_elbow", "right_shoulder", "right_hip", "right_knee", "right_ankle", 
                           "nose"]
                writer.writerow(labels)
                
                # 각 프레임의 관절 좌표를 작성

                for frame_id, keypoints in enumerate(pred_instances): # 일단 현 단계에선 1개의 객체만 볼것. 프레임마다.
                    frame_data = []  # 프레임 ID
                    # 관절 좌표 작성
                    # pred_instances [관절번호==133] [x,y]
                    # 	A	    B   	C		    A	B	C
                    # 0	L손가락	L손목	L팔꿈치		103	9	7
                    # 1	L손목	L팔꿈치	L어깨		9	7	5
                    # 2	L팔꿈치	L어깨	L엉덩		7	5	11
                    # 3	L어깨	L엉덩	L무릎		5	11	13
                    # 4	L엉덩	L무릎	L발목		11	13	15
                    # 5	L무릎	L발목	L발끝		13	15	17
                    # 6	R손가락	R손목	R팔꿈치		124	10	8
                    # 7	R손목	R팔꿈치	R어깨		10	8	6
                    # 8	R팔꿈치	R어깨	R엉덩		8	6	12
                    # 9	R어깨	R엉덩	R무릎		6	12	14
                    # 10R엉덩	R무릎	R발목		12	14	16
                    # 11R무릎	R발목	R발끝		14	16	20
                    # 12R어깨	코  	L어깨	    6	0	5
                    frame_data.append(getAngle(keypoints[103], keypoints[9],  keypoints[7]))
                    frame_data.append(getAngle(keypoints[9],   keypoints[7],  keypoints[5]))
                    frame_data.append(getAngle(keypoints[7],   keypoints[5],  keypoints[11]))
                    frame_data.append(getAngle(keypoints[5],   keypoints[11], keypoints[13]))
                    frame_data.append(getAngle(keypoints[11],  keypoints[13], keypoints[15]))
                    frame_data.append(getAngle(keypoints[13],  keypoints[15], keypoints[17]))
                    frame_data.append(getAngle(keypoints[124], keypoints[10], keypoints[8]))
                    frame_data.append(getAngle(keypoints[10],  keypoints[8],  keypoints[6]))
                    frame_data.append(getAngle(keypoints[8],   keypoints[6],  keypoints[12]))
                    frame_data.append(getAngle(keypoints[6],   keypoints[12], keypoints[14]))
                    frame_data.append(getAngle(keypoints[12],  keypoints[14], keypoints[16]))
                    frame_data.append(getAngle(keypoints[14],  keypoints[16], keypoints[20]))
                    frame_data.append(getAngle(keypoints[6],   keypoints[0],  keypoints[5]))
                    writer.writerow(frame_data)


                    
import argparse
def main(videoname):
    """Visualize the demo images.

    Using mmdet to detect the human.
    """

    args = argparse.Namespace(
        det_config='demo/mmdetection_cfg/faster_rcnn_r50_fpn_coco.py',
        det_checkpoint='https://download.openmmlab.com/mmdetection/v2.0/faster_rcnn/faster_rcnn_r50_fpn_1x_coco/faster_rcnn_r50_fpn_1x_coco_20200130-047c8118.pth',
#         det_checkpoint='./faster_rcnn_r50_fpn_1x_coco_20200130-047c8118.pth',
        pose_config='configs/wholebody_2d_keypoint/topdown_heatmap/coco-wholebody/td-hm_hrnet-w48_dark-8xb32-210e_coco-wholebody-384x288.py',
        pose_checkpoint='https://download.openmmlab.com/mmpose/top_down/hrnet/hrnet_w48_coco_wholebody_384x288_dark-f5726563_20200918.pth',
#         pose_checkpoint='./hrnet_w48_coco_wholebody_384x288_dark-f5726563_20200918.pth',
        
        #좀 덜 좋은 모델
        # det_config='demo/mmdetection_cfg/faster_rcnn_r50_fpn_coco.py',
        # det_checkpoint='https://download.openmmlab.com/mmdetection/v2.0/faster_rcnn/faster_rcnn_r50_fpn_1x_coco/faster_rcnn_r50_fpn_1x_coco_20200130-047c8118.pth',
        # pose_config='configs/wholebody_2d_keypoint/topdown_heatmap/coco-wholebody/td-hm_hrnet-w48_8xb32-210e_coco-wholebody-256x192.py',
        # pose_checkpoint='https://download.openmmlab.com/mmpose/top_down/hrnet/hrnet_w48_coco_wholebody_256x192-643e18cb_20200922.pth',
        input=videoname,
        output_root='vis_results/',
        save_predictions=False,
        device='cuda:0',
        show = False,
        det_cat_id = 0,
        bbox_thr = 0.7,
        nms_thr = 0.7,
        kpt_thr = 0.7,
        draw_heatmap = False,
        show_kpt_idx = False,
        skeleton_style = 'mmpose',
        radius = 3,
        thickness = 1,
        show_interval = 0,
        alpha = 0.8,
        draw_bbox = False,
        make_csv = True
        # save_predictions=False,
        # device='cuda',
        # show = False,
        # det_cat_id = 0,
        # bbox_thr = 0.7,
        # nms_thr = 0.7,
        # kpt_thr = 0.7,
        # draw_heatmap = False,
        # show_kpt_idx = False,
        # skeleton_style = 'mmpose',
        # radius = 3,
        # thickness = 1,
        # show_interval = 0,
        # alpha = 0.8,
        # draw_bbox = False,
        # make_csv = True
    )

    assert args.show or (args.output_root != '')
    assert args.input != ''
    assert args.det_config is not None
    assert args.det_checkpoint is not None

    output_file = None
    if args.output_root:
        mmengine.mkdir_or_exist(args.output_root)
        output_file = os.path.join(args.output_root,
                                   os.path.basename(args.input))
        if args.input == 'webcam':
            output_file += '.mp4'

    if args.save_predictions:
        assert args.output_root != ''
        args.pred_save_path = f'{args.output_root}/results_' \
            f'{os.path.splitext(os.path.basename(args.input))[0]}.json'

    # build detector
    detector = init_detector(
        args.det_config, args.det_checkpoint, device=args.device)


    
    detector.cfg = adapt_mmdet_pipeline(detector.cfg)

    # build pose estimator
    pose_estimator = init_pose_estimator(
        args.pose_config,
        args.pose_checkpoint,
        device=args.device,
        cfg_options=dict(
            model=dict(test_cfg=dict(output_heatmaps=args.draw_heatmap))))

    # build visualizer
    pose_estimator.cfg.visualizer.radius = args.radius
    pose_estimator.cfg.visualizer.alpha = args.alpha
    pose_estimator.cfg.visualizer.line_width = args.thickness
    visualizer = VISUALIZERS.build(pose_estimator.cfg.visualizer)
    # the dataset_meta is loaded from the checkpoint and
    # then pass to the model in init_pose_estimator
    visualizer.set_dataset_meta(
        pose_estimator.dataset_meta, skeleton_style=args.skeleton_style)

    if args.input == 'webcam':
        input_type = 'webcam'
    else:
        input_type = mimetypes.guess_type(args.input)[0].split('/')[0]

    if input_type == 'image':

        # inference
        pred_instances = process_one_image(args, args.input, detector,
                                           pose_estimator, visualizer)

        if args.save_predictions:
            pred_instances_list = split_instances(pred_instances)

        if output_file:
            img_vis = visualizer.get_image()
            if args.show:
                mmcv.imwrite(mmcv.rgb2bgr(img_vis), output_file)

        if args.make_csv:
            save_pred_instances_to_csv(input_type, pred_instances, f'{os.path.join(args.output_root,os.path.basename(args.input.split(".")[0]))}.csv')

#--------------------------------------------------------------------------------------------#
#--------------------------------------------------------------------------------------------#
#--------------------------------------------------------------------------------------------#
#--------------------------------------------------------------------------------------------#

    elif input_type in ['webcam', 'video']:

        if args.input == 'webcam':
            cap = cv2.VideoCapture(0)
        else:
            cap = cv2.VideoCapture(args.input)

        video_writer = None
        pred_instances_list = []
        pred_instances_csv = [] # [frame ,  joint, xy]
        frame_idx = 0
        

        while cap.isOpened():
            success, frame = cap.read()
            frame_idx += 1

            if not success:
                break

            # topdown pose estimation
            pred_instances = process_one_image(args, frame, detector,
                                               pose_estimator, visualizer,
                                               0.001)
            if args.make_csv:
                pred_instances_csv.append(pred_instances['keypoints'][0]) # 일단 현 단계에선 객체1개만 저장.

            if args.save_predictions:
                # save prediction results
                pred_instances_list.append(
                    dict(
                        frame_id=frame_idx,
                        instances=split_instances(pred_instances)))

            # output videos
            if output_file:
                frame_vis = visualizer.get_image()

                if video_writer is None:
                    fourcc = cv2.VideoWriter_fourcc(*'mp4v')
                    # the size of the image with visualization may vary
                    # depending on the presence of heatmaps
                    video_writer = cv2.VideoWriter(
                        output_file,
                        fourcc,
                        60,  # saved fps
                        (frame_vis.shape[1], frame_vis.shape[0]))

                video_writer.write(mmcv.rgb2bgr(frame_vis))

            # press ESC to exit
            if cv2.waitKey(5) & 0xFF == 27:
                break

            time.sleep(args.show_interval)

        if video_writer:
            video_writer.release()

        cap.release()
        # 예측 결과를 CSV 파일로 정리
        if args.make_csv:
            save_pred_instances_to_csv(input_type, pred_instances_csv, f'{os.path.join(args.output_root,os.path.basename(args.input.split(".")[0]))}.csv')

    else:
        args.save_predictions = False
        raise ValueError(
            f'file {os.path.basename(args.input)} has invalid format.')

    if args.save_predictions:
        with open(args.pred_save_path, 'w') as f:
            json.dump(
                dict(
                    meta_info=pose_estimator.dataset_meta,
                    instance_info=pred_instances_list),
                f,
                indent='\t')
        print(f'predictions have been saved at {args.pred_save_path}')
    
    

    return pred_instances


def calculate_score(value): # 0.15 아래는 0점 0.85 위로는 100점.
    if value <= 0.10:
        score = 0
    elif value >= 0.80:
        score = 100
    else:
        score = round((((value - 0.10) / (0.80 - 0.10)) * 100),2)  # 선형적으로 매핑
    return score


def total_score(df_ori,df_com): # 길이가 더 짧은 df에 대해 진행.
    sum_list_ori = []
    sum_list_com = []    

    if len(df_ori)<=len(df_com):
        frame_len = len(df_ori)
    else:
        frame_len = len(df_com)    
    

    # 60행 단위로 분할하고, 각 분할에서 값을 더하기
    for i in range(0, frame_len, 60):

        
        temp_df_ori = df_ori[i:i+60]
        temp_df_com = df_com[i:i+60]
        
        sums_ori = temp_df_ori.mean()
        sums_com = temp_df_com.mean()

        sums_reshaped_ori = np.reshape(sums_ori, (1, 13))
        sums_reshaped_com = np.reshape(sums_com, (1, 13))
        
        sum_list_ori.append(sums_reshaped_ori)
        sum_list_com.append(sums_reshaped_com)

    # 각 분할의 합을 포함하는 새 DataFrame 생성
    df_ori_before_hasing = pd.concat([pd.DataFrame(sums_ori) for sums_ori in sum_list_ori], ignore_index=True)
    df_com_before_hasing = pd.concat([pd.DataFrame(sums_com) for sums_com in sum_list_com], ignore_index=True)

# ---------------------- 위의 과정은 second 단위로 바꾼 상황

    # total score
    df_ori_hasing = df_ori_before_hasing.diff()  # 초 별 diff 구함
    df_com_hasing = df_com_before_hasing.diff() 
    df1_np = df_ori_hasing[1:].values
    df2_np = df_com_hasing[1:].values      

    df1_np_flatten = df1_np.flatten()
    df2_np_flatten = df2_np.flatten()
    # df1_np_flatten = df1_np.flatten(order='F')
    # df2_np_flatten = df2_np.flatten(order='F')


    cosine_similarity = 1 - cosine(df1_np_flatten, df2_np_flatten)

    # temp_score = cosine_similarity(vec1_2d, vec2_2d)

    total_score = calculate_score(cosine_similarity) # 점수화 시킴.

    # joint score
    joint = []
    for joint_idx in range(13): # 관절에 대해 진행
        df1_np = df_ori_hasing[1:][joint_idx].values
        df2_np = df_com_hasing[1:][joint_idx].values      

        df1_np_flatten = df1_np.flatten()
        df2_np_flatten = df2_np.flatten()
        # df1_np_flatten = df1_np.flatten(order='F')
        # df2_np_flatten = df2_np.flatten(order='F')


        cosine_similarity = 1 - cosine(df1_np_flatten, df2_np_flatten)

        # temp_score = cosine_similarity(vec1_2d, vec2_2d)

        joint_score = calculate_score(cosine_similarity) # 점수화 시킴.
        joint.append(joint_score)
    joint_df = pd.DataFrame(joint, columns=['Score']) #들여쓰기 ㅅㅂ
#         joint_df.to_csv('./vis_results/joint.csv',index=False)
    data_list = joint_df["Score"].tolist() #-------------------------------------내코드-------------------------------------
        # JSON 형식으로 변환
    json_joint = {
        "joint_score": data_list
    }
        
    # sec_score
    sec = []
    for sec_idx in range(1,(frame_len//60)+1): # 시간초에 대해 진행
        df1_np = df_ori_hasing[sec_idx:sec_idx+1].values
        df2_np = df_com_hasing[sec_idx:sec_idx+1].values

        df1_np_flatten = df1_np.flatten()
        df2_np_flatten = df2_np.flatten()
        # df1_np_flatten = df1_np.flatten(order='F')
        # df2_np_flatten = df2_np.flatten(order='F')


        cosine_similarity = 1 - cosine(df1_np_flatten, df2_np_flatten)

        # temp_score = cosine_similarity(vec1_2d, vec2_2d)

        sec_score = calculate_score(cosine_similarity) # 점수화 시킴.
        sec.append(sec_score)
        
    sec_df = pd.DataFrame(sec, columns=['Score']) #들여쓰기 ㅅㅂㅆㅃㅆㅃㅆㅃㅆㅃㅆㅃ
#         sec_df.to_csv('./vis_results/time.csv',index=False
    data_list = sec_df["Score"].tolist() #-------------------------------------내코드-------------------------------------
        # JSON 형식으로 변환
    json_time = {
        "time_score": data_list
    }
    return total_score, json_joint, json_time #총점, json형식 관절별 점수, 시간별 점수


import boto3
from flask import Flask, request, jsonify
import json
import requests
import os

app = Flask(__name__)

@app.route('/', methods=['POST'])
def test():
    # AWS 인증 정보 설정
    session = boto3.Session(
        aws_access_key_id="",
        aws_secret_access_key='7'
    )
    
    # S3 클라이언트 생성
    s3 = session.client('s3')
    
    # 버킷 이름
    bucket = 'forshop-bucket'
    
    data = request.get_json()  # 요청 바디에서 JSON 데이터 가져오기
    
    original_videoname = data.get('original_video')
    compare_videoname= data.get('compare_video')
    
    s3.download_file(bucket, original_videoname, './' + original_videoname) 
    s3.download_file(bucket, compare_videoname, './' + compare_videoname) #버킷명, 다운로드 할 파일 이름, 다운로드 받을 파일 경로 및 이름
    
    main(original_videoname)       #동영상에 대한 추론과정 진행
    main(compare_videoname)

    with open('./vis_results/' + original_videoname, 'rb') as data:
        s3.upload_fileobj('./vis_results/' + original_videoname, bucket, 'result_' + original_videoname, ExtraArgs={'ACL': 'public-read'})
    with open('./vis_results/' + compare_videoname, 'rb') as data:
        s3.upload_fileobj('./vis_results/' + compare_videoname, bucket, 'result_' + compare_videoname, ExtraArgs={'ACL': 'public-read'})
    # s3.upload_file('./vis_results/' + original_videoname, bucket, 'result_' + original_videoname, ExtraArgs={'ACL': 'public-read'})
    # s3.upload_file('./vis_results/' + compare_videoname, bucket, 'result_' + compare_videoname, ExtraArgs={'ACL': 'public-read'})

    ori_filename = os.path.splitext(original_videoname)[0] #비디오 확장자 제거(파일명만 가져오기)
    com_filename = os.path.splitext(compare_videoname)[0] #비디오 확장자 제거(파일명만 가져오기)
    
    df_ori = pd.read_csv('./vis_results/' + ori_filename + '.csv') #csv 데이터프레임으로 저장
    df_com = pd.read_csv('./vis_results/' + com_filename + '.csv')
    
    t_score, json_joint, json_time = total_score(df_ori,df_com) #총점, json 가져오기
    
    # 응답 비디오 json 데이터 생성
    json_videoname = {'result_original_videoname' : 'result_' + original_videoname,
                'result_compare_videoname' : 'result_' + compare_videoname,
                'total_score' : t_score,
               }
    
    result_json = {**json_videoname, **json_joint, **json_time}
    
    return jsonify(result_json)
    
if __name__ == '__main__':
    app.run(host='0.0.0.0', port=7070)