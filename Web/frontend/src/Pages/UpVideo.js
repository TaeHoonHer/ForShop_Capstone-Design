import React, { useState } from 'react';
import styled from 'styled-components';
import DetailHeader from '../Components/DetailHeader';
import UpVdForm from '../Components/UpVdForm';
import '../Css/UpVideo.css';
import { Link } from 'react-router-dom';

const UpVdFormWrapper = styled.div`
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

function UpVideo() {
  const [popupVisible, setPopupVisible] = useState(false);

  const handlePopupOpen = () => {
    setPopupVisible(true);
  };

  const handlePopupClose = () => {
    setPopupVisible(false);
  };

    return (
        <UpVdFormWrapper>
            <DetailHeader />
            <BackgroundOverlay />
            <FormContainer className='FormContainer'>
                <FormHead>
                    <div className='formhead'>
                        <Link to ="/upload">
                            <LeftArrow img src ="/img/left-arrow.png"/>
                        </Link>
                        <h2>Video Upload</h2>
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
                <UpVdForm />
                <CheckBtn className="CheckBtn">
                    <button type='button' onClick={handlePopupOpen} className='check'>Go!</button>
                </CheckBtn>
                <PopupWrapper visible={popupVisible}>
                  <div class = "popup">
                    <img src = "/img/checked.png" alt="Success"/>
                    <h2>Success!</h2>
                    <p>Your upload has been successfully submitted!</p>
                    <Link to ="/main">
                      <button type = "button" class = "chBtn" onClick={handlePopupClose}>OK</button>
                    </Link>
                  </div>
                </PopupWrapper>
            </FormContainer>
        </UpVdFormWrapper>
    )
}

export default UpVideo;