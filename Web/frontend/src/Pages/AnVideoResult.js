import React, { useState, useEffect } from 'react';
import styled from 'styled-components';
import '../Css/AnVideoResult.css';
import Footer from '../Components/Footer';
import DetailHeader from '../Components/DetailHeader';
import Chart from '../Components/Chart';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';


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
    const [videoRes, setVideoRes] = useState(null);

    useEffect(() => {
      const fetchVideoData = async () => {
        try {
          const accessToken = localStorage.getItem('accessToken');
          
          const response = await axios.get('/api/video_result', {
            headers: {
              'Authorization': `Bearer ${accessToken}`
            }
          });

          if (response.status === 200) {
            setVideoRes(response.data);
          }
          else {
            console.error("Response status is not 200");
          }
        } catch (error) {
          console.error(error);
        }
      };

      fetchVideoData();
    }, []);

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
              {videoRes && <video src = {videoRes.video} autoPlay muted loop/>}
              {videoRes && <p>Rate : &nbsp;&nbsp;&nbsp;&nbsp;{videoRes.rate} Points</p>}
            </div>
            <div className='ResultDetails'>
              <div className='chartBox'>
                {videoRes && <Chart time={videoRes.time} rate={videoRes.rate}/>}
              </div>
              <div className='detailBox'>
                <button type='button' onClick={() => navigate('/anvideo')}>Again?</button>
                <button type='button' onClick={() => navigate(`/upimg?video=${encodeURIComponent(videoRes.video)}`)}>Upload?</button>
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