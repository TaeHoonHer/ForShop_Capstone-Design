import React from 'react';
import styled from 'styled-components';
import '../Css/ResultChart.css';
import Chart from '../Components/Chart';
import { useNavigate } from 'react-router-dom';


const ChartWrapper = styled.div`
    position : relative;
    width : 100vw;
    min-height : 150vh;
    margin : 0;
    padding : 0;
    justify-content : center;
    align-items : center;
`;

const ChartContainer = styled.div`
    position : relative;
    width : 90%;
    height : 90%;
    margin : 0;
    padding : 0;
    display : flex;
    flex-direction : column;
    justify-content : center;
    align-items : center;
`;

function ResultChart () {
    const navigate = useNavigate();

    const handleBack = () => {
        navigate('/anvideo');
    };
    

  return (
    <ChartWrapper>
      <ChartContainer>
        <div className='chartBox1'>
            <div className='cb1Title'>
                <p>종합 차트</p>
            </div>
            <div className='cb1Contents'>
                <Chart/>
            </div>
        </div>
        <div className='chartBox2'>
            <div className='cbBox'>
                <div className='cb2Title'>
                    <p>시간 차트</p>
                </div>
                <div className='cb2Contents'>
                    <Chart/>
                </div>
            </div>
            <div className='cbBox'>
                <div className='cb2Title'>
                    <p>각도 차트</p>
                </div>
                <div className='cb2Contents'>
                    <Chart/>
                </div>
            </div>
        </div>
        <div className='AgainBtn'>
            <button type = 'button' className='againBtn' onClick={handleBack}>Again?</button>
        </div>
      </ChartContainer>
    </ChartWrapper>
  );

}

export default ResultChart;