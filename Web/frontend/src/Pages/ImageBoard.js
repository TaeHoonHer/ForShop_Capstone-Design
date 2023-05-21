import React from 'react';
import styled from 'styled-components';
import { Link } from 'react-router-dom';
import DetailHeader from '../Components/DetailHeader';
import Footer from '../Components/Footer';
import ImgBoardForm from '../Components/ImgBoardForm';

const BoardWrapper = styled.div`
    width : 100vw;
    height : 100vh;
    overflow : hidden;
`;

function ImageBoard () {
    return (
        <BoardWrapper>
            <DetailHeader/>
            <ImgBoardForm/>
            <Footer/>
        </BoardWrapper>
    );
}

export default ImageBoard;