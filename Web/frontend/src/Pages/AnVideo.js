import React, { useState, useEffect, useRef } from 'react';
import styled from 'styled-components';
import DetailHeader from '../Components/DetailHeader';
import AnVideoForm from '../Components/AnVideoForm';
import '../Css/AnVideo.css';
import { Link, useNavigate } from 'react-router-dom';
import axios from 'axios';

const AnVdFormWrapper = styled.div`
  overflow : hidden;
  width: 100vw;
  min-height: 100vh;
  background: #fff;
  padding: 0;
  margin: 0;
  position: relative;
`;

const BackgroundOverlay = styled.div`
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(1px);
  z-index: 1;
`;

const FormContainer = styled.div`
  width: 70%;
  height: 70%;
  top: 50%;
  left: 50%;
  position: absolute;
  transform: translate(-50%, -50%);
  display: flex;
  flex-direction: column;
  align-items: center;
  background: linear-gradient(to top, #ffffff 80%, #f7c3fc);
  backdrop-filter: blur(5px);
  border: none;
  border-radius: 20px;
  z-index: 2;
`;

const FormHead = styled.div`
  position : relative;
  margin: 1% 0;
  padding: 0;
  width: 100%;
  height: 50px;
  display: flex;
  justify-content: center;
  align-items: center;

  h2 {
    font-size: 2rem;
    text-align: center;
    letter-spacing: 2px;
    color: black;
    margin: 0;
  }
`;

const LeftArrow = styled.img`
  position : absolute;
  top : 16px;
  left : 30px;
  width: 20px;
  height: 20px;
  margin-right: 10px;
  cursor : pointer;
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

function AnVideo() {
  const [videoFile1, setVideoFile1] = useState(null);
  const [videoFile2, setVideoFile2] = useState(null);

  const [showProgress, setShowProgress] = useState(false);
  const [progressValue, setProgressValue] = useState(0);
  const intervalRef = useRef();

  const navigate = useNavigate();

  const startProgress = async () => {

    if (!videoFile1 || !videoFile2) {
      alert('Please upload all files.');
      return;
    }
  
    setShowProgress(true);
  
    const formData = new FormData();
    formData.append('video1', videoFile1);
    formData.append('video2', videoFile2);
  
    const accessToken = localStorage.getItem('accessToken');
  
    // POST video files to server
    try {
      const response = await axios.post('/api/uploadVideo', formData, {
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

    // Component unmount시에 interval을 clear
    return () => {
      clearInterval(intervalRef.current);
    };
  }, [progressValue, navigate]);

    return (
        <AnVdFormWrapper>
            <DetailHeader />
            <BackgroundOverlay />
            <FormContainer className='FormContainer'>
                <FormHead>
                    <div className='formhead'>
                        <Link to ="/upload">
                            <LeftArrow img src ="/img/left-arrow.png"/>
                        </Link>
                        <h2>Analysis</h2>
                    </div>
                </FormHead>
                <AnVideoForm
                  onUpload={(video1, video2) => {
                    setVideoFile1(video1);
                    setVideoFile2(video2);
                  }}
                />
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
            </FormContainer>
        </AnVdFormWrapper>
    )
}

export default AnVideo;