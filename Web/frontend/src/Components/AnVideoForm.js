import React, { useState, useCallback, useEffect, useRef } from 'react';
import styled from 'styled-components';
import '../Css/AnVideoForm.css';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const FormBox = styled.form`
  width: 100%;
  height: calc(100% - 50px);
  margin: 0;
  padding: 0;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;
`;

const VideoBox = styled.div`
  width: 50%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
  border : 2px solid #f386fd;
  border-radius : 30px;
  border-style-type : dotted;
`;

const Video = styled.div`
  width: 50%;
  height: 50%;
  object-fit: contain;
  margin-top: 20px;

  video {
    width: 100%;
    height: 100%;
    object-fit: contain;
  }
`;

const UploadPrompt = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-align: center;
  width: 50%;
  height: 50%;
  h2 {
    margin-top: 70px;
    font-size: 1rem;
  }
`;

const FormContents = styled.div`
  width: 100%;
  height: 80%;
  display: flex;
`;

const CheckBtn = styled.button`
  width: 80px;
  height: 50px;
  margin : 0;
  background: #f386fd;
  backdrop-filter: blur(20px);
  border-radius : 20px;
  color: white;
  border: none;
  cursor : pointer;
  button {
    background: transparent;
    font-size: 15px;
    border : none;
    font-size : 17px;
    color : white;
    cursor : pointer;
  }
`;

const ProgressBox = styled.div`
  display : flex;
  flex-direction : column;
  margin : 0;
  padding : 0;
  justify-content : center;
  align-items : center;
`;

const ProgressBarWrapper = styled.div`
  position: absolute;
  bottom: -10px;
  width: 80%;
  height: 30px;
  z-index: 3;
  background-color: white;
  border : 2px solid #f386fd;
  box-sizing: border-box; // Include padding and border in the element's total width and height
  border-radius : 30px;
  box-shadow : 2px 4px 10px #ddd;
`;

const ProgressBar = styled.div`
  width: ${({ value }) => `${value}%`};
  height: 100%;
  background-image: linear-gradient(#f386fd, #efc0f3);
  transition: width 0.5s ease-in-out;
  box-sizing: border-box; // Include padding and border in the element's total width and height
  border-radius : 30px;
`;

function AnVideoForm() {
  const [videoFile1, setVideoFile1] = useState(null); // 첫 번째 비디오 파일 상태
  const [videoFile2, setVideoFile2] = useState(null); // 두 번째 비디오 파일 상태
  const [showProgress, setShowProgress] = useState(false);
  const [progressValue, setProgressValue] = useState(0);
  const intervalRef = useRef();
  const navigate = useNavigate();

  const handleFileChange = (event, index) => { // index 추가하여 어떤 드롭 영역에서 파일을 선택했는지 구분
    const file = event.target.files[0];
    handleFileUpload(file, index);
  };

  const handleFileUpload = useCallback((file, index) => {
    if (file) {
      // Check if the file is an MP4 video
      if (file.type !== 'video/mp4') {
        alert('영상 데이터만 가능합니다.');
        return;
      }
  
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onloadend = () => {
        if (index === 1) {
          setVideoFile1(reader.result); // 첫 번째 드롭 영역의 파일 업로드 상태를 변경
        } else if (index === 2) {
          setVideoFile2(reader.result); // 두 번째 드롭 영역의 파일 업로드 상태를 변경
        }
      };
    } else {
      alert('No file chosen.');
    }
  }, []);

  const handleDrop = useCallback((event, index) => { // index 추가하여 어떤 드롭 영역에서 파일을 드롭했는지 구분
    event.preventDefault();
    const file = event.dataTransfer.files[0];
    handleFileUpload(file, index);
  }, [handleFileUpload]);

  const handleDragOver = (event) => {
    event.preventDefault();
  };

  const startProgress = async () => {

    if (!videoFile1 || !videoFile2) {
      alert('Please upload all files.');
      return;
    }
  
    setShowProgress(true);
  
    const formData = new FormData();
    formData.append('originalVideo', videoFile1);
    formData.append('compareVideo', videoFile2);
  
    const accessToken = localStorage.getItem('accessToken');
  
    // POST video files to server
    try {
      const response = await axios.post('/api/analyzing/upload', formData, {
        headers: {
          'Authorization': `Bearer ${accessToken}`,
          'Content-Type': 'multipart/form-data',
        },
        onUploadProgress: function(progressEvent) {
          let percentCompleted = Math.round((progressEvent.loaded * 100) / progressEvent.total);
          setProgressValue(percentCompleted);
        }
      });
      
      if (response.data.status !== 200) {
        console.log("Failed to upload videos due to server error.");
        return;
      }
      
      console.log(response);
    } catch (error) {
      console.error(error);
      return;
    }
  };

  useEffect(() => {
    if (progressValue >= 100) {
      clearInterval(intervalRef.current);
      setShowProgress(false);
      navigate("/anvideo/result");
    }
  
    return () => {
      clearInterval(intervalRef.current);
    };
  }, [progressValue, navigate]);

  return (
    <FormBox>
      <FormContents>
        <VideoBox onDrop={(event) => handleDrop(event, 1)} onDragOver={handleDragOver}> {/* 첫 번째 드롭 영역 */}
          {videoFile1 ? (
            <Video>
              <video controls autoPlay>
                <source src={videoFile1} type='video/mp4' />
              </video>
            </Video>
          ) : (
            <UploadPrompt>
              <Video>
                <img src="/img/videoIcon.png" />
              </Video>
              <h2>비디오를 여기에 드래그 하세요.</h2>
              <label htmlFor="file-upload1" className="custom-file-upload">
                파일 선택
                <input id="file-upload1" type="file" onChange={(event) => handleFileChange(event, 1)} />
              </label>
            </UploadPrompt>
          )}
        </VideoBox>
        <VideoBox onDrop={(event) => handleDrop(event, 2)} onDragOver={handleDragOver}> {/* 두 번째 드롭 영역 */}
          {videoFile2 ? (
            <Video>
              <video controls autoPlay>
                <source src={videoFile2} type='video/mp4' />
              </video>
            </Video>
          ) : (
            <UploadPrompt>
              <Video>
                <img src="/img/videoIcon.png" />
              </Video>
              <h2>비디오를 여기에 드래그 하세요.</h2>
              <label htmlFor="file-upload2" className="custom-file-upload">
                파일 선택
                <input id="file-upload2" type="file" onChange={(event) => handleFileChange(event, 2)} />
              </label>
            </UploadPrompt>
          )}
        </VideoBox>
      </FormContents>
      {showProgress ? (
        <ProgressBox>
          <p style={{ color : 'black'}}>{progressValue < 100 ? 'Loading...' : 'Upload complete!'}</p>
          <ProgressBarWrapper>
            <ProgressBar value={progressValue} />
          </ProgressBarWrapper>
        </ProgressBox>
        ) : (
        <CheckBtn className="CheckBtn">
          <button type='button' onClick={startProgress} className='check'>Start!</button>
        </CheckBtn>
      )}
    </FormBox>
  );
}
export default AnVideoForm;