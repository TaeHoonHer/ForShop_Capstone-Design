import React, { useState, useCallback } from 'react';
import styled from 'styled-components';
import '../Css/AnVideoForm.css';
import { useNavigate } from 'react-router-dom';
import '../Css/LoadingScreen.css';
import { useDispatch } from 'react-redux';

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

const LoadingText = styled.p`
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 10;
  background: linear-gradient(to right, #f386fd, gold);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
`;

function AnVideoForm() {
  const [videoFile1, setVideoFile1] = useState(null);
  const [videoFile2, setVideoFile2] = useState(null);
  const [fileObject1, setFileObject1] = useState(null);
  const [fileObject2, setFileObject2] = useState(null);
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();

  const dispatch = useDispatch();

  const handleFileChange = (event, index) => {
    const file = event.target.files[0];
    handleFileUpload(file, index);
  };

  const handleFileUpload = useCallback((file, index) => {
    if (file) {
      if (file.type !== 'video/mp4') {
        alert('영상 데이터만 가능합니다.');
        return;
      }

      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onloadend = () => {
        if (index === 1) {
          setVideoFile1(reader.result);
          setFileObject1(file);  // Save the file object
        } else if (index === 2) {
          setVideoFile2(reader.result);
          setFileObject2(file);  // Save the file object
        }
      };
    } else {
      alert('No file chosen.');
    }
  }, []);

  const handleDrop = useCallback((event, index) => {
    event.preventDefault();
    const file = event.dataTransfer.files[0];
    handleFileUpload(file, index);
  }, [handleFileUpload]);

  const handleDragOver = (event) => {
    event.preventDefault();
  };

  const startProgress = () => {
    setIsLoading(true);
  
    setTimeout(() => {
      setIsLoading(false);
      navigate('/anvideo/result');
    }, 5000);
  };

  return (
    <FormBox>
      {isLoading ? (
        <div class="lds-roller">
          <div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div>
          <LoadingText>Analyzing...</LoadingText>
        </div>
      ) : (
        // Main component
      <>
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
      <CheckBtn className="CheckBtn">
        <button type='button' onClick={startProgress} className='check'>Start!</button>
      </CheckBtn>
      </>
      )}
    </FormBox>
  );
}
export default AnVideoForm;
