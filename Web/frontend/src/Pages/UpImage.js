import React from 'react';
import styled from 'styled-components';
import DetailHeader from '../Components/DetailHeader';
import UpImgForm from '../Components/UpImgForm';
import '../Css/UpImage.css';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { ToastContainer } from 'react-toastify';
import { Link } from 'react-router-dom';

const UpImgFormWrapper = styled.div`
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
  margin-top : 50px;
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
    font-size : 20px;
    color : white;
    cursor : pointer;
  }
`;

const handleButtonClick = () => {
    toast.info('업로드 하시겠습니까?', {
      position: toast.POSITION.TOP_CENTER,
      autoClose: false,
      hideProgressBar: true,
      closeOnClick: true,
      pauseOnHover: true,
      draggable: true,
      progress: undefined,
    });
  };

function UpImage() {
    return (
        <UpImgFormWrapper>
            <DetailHeader />
            <BackgroundOverlay />
            <FormContainer className='FormContainer'>
                <FormHead>
                    <div className='formhead'>
                        <Link to ="/main">
                            <LeftArrow img src ="/img/left-arrow.png"/>
                        </Link>
                        <h2>Image Upload</h2>
                    </div>
                </FormHead>
                <UpImgForm />
                <CheckBtn className="CheckBtn">
                    <button type='button' onclick={handleButtonClick} className='check'>OK!</button>
                </CheckBtn>
            </FormContainer>
            <ToastContainer />
        </UpImgFormWrapper>
    )
}

export default UpImage;