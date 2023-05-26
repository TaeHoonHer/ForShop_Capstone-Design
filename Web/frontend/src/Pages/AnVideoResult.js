import React from 'react';
import styled from 'styled-components';
import '../Css/AnVideoResult.css';
import Footer from '../Components/Footer';
import DetailHeader from '../Components/DetailHeader';
import Chart from '../Components/Chart';


const ResultWrapper = styled.div`
    width : 100vw;
    height : 100vh;
    margin : 0;
    padding : 0;
    background-color : white;
    justify-content : center;
    align-items : center;
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

const ResultContainer = styled.div`
    width : 100%;
    height : 100%;
    margin : 0;
    padding : 0;
    display : flex;
    justify-content : center;
    align-items : center;
    z-index : 3;
`;

function AnVideoResult () {
    return (
        <ResultWrapper>
            <DetailHeader/>
            <BackgroundOverlay />
            <ResultContainer>
                <div className='resultWrapper'>
                    <div className='resultTitle'>
                        <p>Analysis of Result</p>
                    </div>
                    <div className='ResultBox'>
                        <div className='ResultContents'>
                            <video src = '/img/banner.mp4' autoPlay muted/>
                            <p>0 점</p>
                        </div>
                        <div className='ResultDetails'>
                            <div className='chartBox'>
                                <Chart/>
                            </div>
                            <div className='detailBox'>
                                <button type='button' onClick=''>Again?</button>
                                <button type='button' onClick=''>Upload?</button>
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