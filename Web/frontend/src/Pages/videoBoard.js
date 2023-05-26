import React from 'react';
import styled from 'styled-components';
import DetailHeader from '../Components/DetailHeader';
import Footer from '../Components/Footer';
import VdBoardForm from '../Components/VdBoardForm';

const BoardWrapper = styled.div`
    width : 100vw;
    min-height : 100vh;
    overflow-x : hidden;
`;

function VideoBoard () {
    return (
        <BoardWrapper>
            <DetailHeader/>
            <VdBoardForm/>
            <Footer/>
        </BoardWrapper>
    );
}

export default VideoBoard;