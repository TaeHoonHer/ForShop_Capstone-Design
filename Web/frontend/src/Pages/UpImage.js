import React, { useState } from 'react';
import styled from 'styled-components';
import DetailHeader from '../Components/DetailHeader';
import UpImgForm from '../Components/UpImgForm';
import '../Css/UpImage.css';
import { Link, useLocation } from 'react-router-dom';
import axios from 'axios';

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
    font-size : 20px;
    color : white;
    cursor : pointer;
  }
`;

const PopupWrapper = styled.div`
  position: absolute;
  top: 50%;
  left: 50%;
  transition : transform 0.4s, top 0.4s;
  transform: translate(-50%, -50%);
  visibility: ${props => (props.visible ? 'visible' : 'hidden')};
  transform: ${props => (props.visible ? 'translate(-50%, -50%) scale(10)' : 'translate(-50%, -50%) scale(0)')};
`;

function UpImage() {
  const [popupVisible, setPopupVisible] = useState(false);

  const location = useLocation();
  const videoData = new URLSearchParams(location.search).get("video");

  const handlePopupOpen = () => {
    setPopupVisible(true);
  };

  const handlePopupClose = () => {
    setPopupVisible(false);
  };

  const handleUpload = async (data) => {
    const formData = new FormData();
    formData.append('title', data.title);
    formData.append('hashtag', data.hashtag);
    formData.append('contents', data.contents);
    formData.append('mediaFile', data.mediaFile);

    const accessToken = localStorage.getItem('accessToken');

    try {
      const response = await axios.post("YOUR_SERVER_URL", formData, {
        headers: {
          'Authorization': `Bearer ${accessToken}`,
          'Content-Type': 'multipart/form-data',
        }
      });

      if (response.status === 200) {
        handlePopupOpen(); 
      } else {
        console.error("Response status is not 200");
      }
    } catch (err) {
      console.error(err);
    }
  };

  return (
      <UpImgFormWrapper>
          <DetailHeader />
          <BackgroundOverlay />
          <FormContainer className='FormContainer'>
              <FormHead>
                  <div className='formhead'>
                      <Link to ="/upload">
                          <LeftArrow img src ="/img/left-arrow.png"/>
                      </Link>
                      <h2>Upload</h2>
                  </div>
              </FormHead>
              <UpImgForm video={videoData} onUpload={handleUpload}/>
              <CheckBtn className="CheckBtn">
                  <button type='button' onClick={handlePopupOpen} className='check'>Go!</button>
              </CheckBtn>
              <PopupWrapper visible={popupVisible}>
                <div class = "popup" id = "popup">
                  <img src = "/img/checked.png" alt="Success"/>
                  <h2>Success!</h2>
                  <p>Your upload has been successfully submitted!</p>
                  <Link to ="/main">
                    <button type = "button" class = "chBtn" onClick={handlePopupClose}>OK</button>
                  </Link>
                </div>
              </PopupWrapper>
          </FormContainer>
      </UpImgFormWrapper>
  )
}

export default UpImage;