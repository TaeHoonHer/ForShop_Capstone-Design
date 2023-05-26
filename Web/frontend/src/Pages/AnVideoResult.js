import React from 'react';
import styled from 'styled-components';
import '../Css/AnVideoResult.css';
import Footer from '../Components/Footer';
import DetailHeader from '../Components/DetailHeader';
import Chart from '../Components/Chart';
import { useNavigate } from 'react-router-dom';


const ResultWrapper = styled.div`
    position : relative;
    width : 100vw;
    height : 90vh;
    margin : 0;
    padding : 0;
    background-color : white;
    justify-content : center;
    align-items : center;
`;

const ResultContainer = styled.div`
    position : relative;
    width : 100%;
    height : 100%;
    margin : 0;
    padding : 0;
    display : flex;
    justify-content : center;
    align-items : center;
`;

function AnVideoResult () {
    const navigate = useNavigate();

    const videoData = '/img/banner.mp4';

    return (
        <ResultWrapper>
            <DetailHeader/>
            <ResultContainer>
                <div className='resultWrapper'>
                    <div className='resultTitle'>
                        <p>Result of Analysis</p>
                    </div>
                    <div className='ResultBox'>
                        <div className='ResultContents'>
                            <video src = {videoData} autoPlay muted loop/>
                            <p>Rate : &nbsp;&nbsp;&nbsp;&nbsp;0 Points</p>
                        </div>
                        <div className='ResultDetails'>
                            <div className='chartBox'>
                                <Chart/>
                            </div>
                            <div className='detailBox'>
                                <button type='button' onClick={() => navigate('/anvideo')}>Again?</button>
                                <button type='button' onClick={() => navigate(`/upimg?video=${encodeURIComponent(videoData)}`)}>Upload?</button>
                            </div>
                        </div>
                    </div>
                </div>
            </ResultContainer>
            <Footer/>
        </ResultWrapper>
    );

}

export default AnVideoResult;