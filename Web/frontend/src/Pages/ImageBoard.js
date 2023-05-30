import React from 'react';
import { useLocation } from 'react-router-dom';
import styled from 'styled-components';
import DetailHeader from '../Components/DetailHeader';
import Footer from '../Components/Footer';
import ImgBoardForm from '../Components/ImgBoardForm';

const BoardWrapper = styled.div`
    width : 100vw;
    min-height : 100vh;
    overflow-x : hidden;
`;

function ImageBoard () {
    const location = useLocation();
    const image = location.state?.image || {};

    return (
        <BoardWrapper>
            <DetailHeader/>
            <ImgBoardForm imageId={image.id} />
            <Footer/>
        </BoardWrapper>
    );
}

export default ImageBoard;
