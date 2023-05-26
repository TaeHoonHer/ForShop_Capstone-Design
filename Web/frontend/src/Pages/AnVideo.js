import React, { useState, useEffect, useRef } from 'react';
import styled from 'styled-components';
import DetailHeader from '../Components/DetailHeader';
import AnVideoForm from '../Components/AnVideoForm';
import '../Css/AnVideo.css';
import { Link, useNavigate } from 'react-router-dom';

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

const AnalysisBox = styled.div`
  width: 100%;
  height: 75%;
  border : none;
  box-shadow : 1px 3px 4px #ddd;
  border-radius : 0 0 20px 20px;
  display: ${({ show }) => (show ? 'block' : 'none')};
  z-index : 3;
`;

function AnVideo() {
  const [videoFile1, setVideoFile1] = useState(null);
  const [videoFile2, setVideoFile2] = useState(null);

  const [showProgress, setShowProgress] = useState(false);
  const [progressValue, setProgressValue] = useState(0);
  const [showAnalysisBox, setShowAnalysisBox] = useState(false);
  const intervalRef = useRef();

  const navigate = useNavigate();

  const startProgress = () => {

    if (!videoFile1 || !videoFile2) {
      alert('파일을 모두 업로드 해주세요.');
      return;
    }

    setShowProgress(true);
    setShowAnalysisBox(true);
    
    const interval = setInterval(() => {
      setProgressValue((prevValue) => {
        // If the value is already 100, return it as is
        if (prevValue >= 100) {
          clearInterval(interval);
          return prevValue;
        }
        // Otherwise, increment the value
        return prevValue + 10;
      });
    }, 1000);
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
                  isVisible={!showAnalysisBox}
                  onUpload={(video1, video2) => {
                    setVideoFile1(video1);
                    setVideoFile2(video2);
                  }}
                  />
                <AnalysisBox show={showAnalysisBox}>
                  룰루
                </AnalysisBox>
                {showProgress ? (
                  <ProgressBox>
                    <p>Loading...</p>
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