import React from 'react';
import styled from 'styled-components';
import MainHeader from '../Components/MainHeader';
import UpImgForm from '../Components/UpImgForm';

const UpImgFormWrapper = styled.div`
    width: 100vw;
    min-height: 100vh;
    background : #fff;
    padding: 0;
    margin: 0;
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
`;

const FormHead = styled.div`
    width: 100%;
    height: 50px;
    h2 {
        font-size: 20px;
        text-align: center;
        letter-spacing: 2px;
        color: black;
    }
`;

const CheckBtn = styled.button`
    width: 50px;
    height: 25px;
    margin-top : 50px;
    background: skyblue;
    color: white;
    border: none;
    button {
        font-size: 15px;
    }
`;

function UpImage() {
    return (
        <UpImgFormWrapper>
            <MainHeader />
            <FormContainer>
                <FormHead>
                    <div className='formhead'>
                        <h2>이미지 업로드</h2>
                    </div>
                </FormHead>
                <UpImgForm />
                <CheckBtn>
                    <button type='button' onclick='' className='check'>확인</button>
                </CheckBtn>
            </FormContainer>
        </UpImgFormWrapper>
    )
}

export default UpImage;