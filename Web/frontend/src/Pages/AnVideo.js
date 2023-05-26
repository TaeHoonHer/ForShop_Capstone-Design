import React, { useState, useEffect, useRef } from 'react';
import styled from 'styled-components';
import DetailHeader from '../Components/DetailHeader';
import AnVideoForm from '../Components/AnVideoForm';
import '../Css/AnVideo.css';
import { Link, useNavigate } from 'react-router-dom';
import { useAnimations } from '@react-three/drei';

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
    position : absolute;
    top : 0;
    left : 38%;
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

const SelectForm = styled.div`
  position : absolute;
  top : 13px;
  left : 83%;
  width : 100%;
  height : 50px;
  margin : 0;
  padding : 0;
  display : flex;

  button {
    background : transparent;
    border : none;
    cursor : pointer;
    font-size : 15px;
  }
`;

const SelLink = styled(Link)`
  background : transparent;
  border : none;
  margin-left : 10px;
  padding-right : 10px;
`;

const ProgressBarWrapper = styled.div`
  position: absolute;
  bottom: -10px;
  width: 80%;
  height: 50px;
  z-index: 3;
  background-color: #f386fd;
  box-sizing: border-box; // Include padding and border in the element's total width and height
  border-radius : 30px;
`;

const ProgressBar = styled.div`
  width: ${({ value }) => `${value}%`};
  height: 100%;
  background-color: #4caf50;
  transition: width 0.5s ease-in-out;
  box-sizing: border-box; // Include padding and border in the element's total width and height
  border-radius : 30px;
`;

const AnalysisBox = styled.div`
  width: 100%;
  height: 75%;
  border: 2px solid #f386fd;
  border-radius: 15px;
  display: ${({ show }) => (show ? 'block' : 'none')};
  z-index : 3;
`;

function AnVideo() {

  const [showProgress, setShowProgress] = useState(false);
  const [progressValue, setProgressValue] = useState(0);
  const [showAnalysisBox, setShowAnalysisBox] = useState(false);
  const intervalRef = useRef();

  const navigate = useNavigate();

  const startProgress = () => {
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
                        <h2>Video Analyze</h2>
                        <SelectForm>
                          <SelLink to = '/upvideo'>
                            <button type = "button" className='upMenu'>UpLoad</button>
                          </SelLink>
                          <SelLink to = '/anvideo'>
                            <button type = "button" className='anMenu'>Analyze</button>
                          </SelLink>
                        </SelectForm>
                    </div>
                </FormHead>
                <AnVideoForm isVisible={!showAnalysisBox}/>
                <AnalysisBox show={showAnalysisBox}>
                  룰루
                </AnalysisBox>
                {showProgress ? (
                  <ProgressBarWrapper>
                    <ProgressBar value={progressValue} />
                  </ProgressBarWrapper>
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