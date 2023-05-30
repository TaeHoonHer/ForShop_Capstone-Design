import React from 'react';
import styled from 'styled-components';
import DetailHeader from '../Components/DetailHeader';
import AnVideoForm from '../Components/AnVideoForm';
import '../Css/AnVideo.css';
import { Link } from 'react-router-dom';

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



function AnVideo() {
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
                <AnVideoForm/>
            </FormContainer>
        </AnVdFormWrapper>
    )
}

export default AnVideo;