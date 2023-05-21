import React from 'react';
import styled from 'styled-components';
import { keyframes } from 'styled-components';
import { Link } from 'react-router-dom';
import DetailHeader from '../Components/DetailHeader';
import Footer from '../Components/Footer';
import '../Css/Upload.css';

const FadeInAnimation = keyframes`
    from {
        opacity: 0;
        transform: translateY(20px);
    }
    to {
        opacity: 1;
        transform: translateY(0);
    }
`;

const UploadHeaderWrapper = styled.div`
    overflow: hidden;
    width : 100vw;
    height : 100vh;
    margin : 0;
    padding : 0;
    background : #fff;
`;

const MenuList = styled.div`
    width : 80%;
    height : 80%;
    margin : 0;
    padding : 0;
    display : flex;
    justify-content: flex-end;
    align-items : center;
    list-style-type : none;
`;

const MenuItem = styled.li`
    position : relative;
    width : 40%;
    height : 40%;
    margin : 0;
    padding : 0;
`;

const ImgLink = styled(Link)`
    width : 100%;
    opacity: 0;
    animation: ${FadeInAnimation} 0.8s ease forwards;
    animation-delay: 0.1s;

    img {
        width : 100%;
        object-fit : cover;
    }
    p {
        position : absolute;
        top : 50%;
        left : 35%;
        font-size : 3rem;
        color : black;
        font-family : 'Poppins';
    }
`;

const VdLink = styled(Link)`
    margin-left : 40px;
    width: 100%;
    opacity: 0;
    animation: ${FadeInAnimation} 0.8s ease forwards;
    animation-delay: 0.1s;

    video {
        width: 100%;
        height : 100%;
        object-fit: cover;
    }
    p {
        position : absolute;
        top : 50%;
        left: 47%;
        font-size: 3rem;
        color: white;
        font-family: 'Poppins';
    }
`;

function Upload() {

    return (
        <UploadHeaderWrapper>
            <DetailHeader/>
            <MenuList>
                <MenuItem>
                    <li>
                        <ImgLink to = '/upimg' className='imglink'>
                            <img src='/img/person.jpg' alt='image button'/>
                            <p className='selOption'>Image</p>
                        </ImgLink>
                    </li>
                </MenuItem>
                <MenuItem>
                    <li>
                        <VdLink to = '/upvideo' className='vdlink'>
                            <video autoPlay muted loop>
                                <source src="../img/dance.mp4" type="video/mp4"></source>
                            </video>
                            <p className='selOption'>Video</p>
                        </VdLink>
                    </li>
                </MenuItem>
            </MenuList>
            <Footer/>
        </UploadHeaderWrapper>
    )
}

export default Upload;