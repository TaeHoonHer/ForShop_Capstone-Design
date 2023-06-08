import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import '../Css/ResultChart.css';
import {Chart, Chart2} from './Chart';


const ChartWrapper = styled.div`
    position : relative;
    width : 100vw;
    height : 70vh;
    margin : 0;
    padding : 0;
    justify-content : center;
    align-items : center;
`;

const ChartContainer = styled.div`
    position : relative;
    width : 100%;
    height : 100%;
    margin : 0;
    padding : 0;
    display : flex;
    flex-direction : column;
    justify-content : center;
    align-items : center;
`;

function ResultChart () {

  const time_score = [97,69,80,97,96,89,87,97,85,77,54,77,54,86,54]
  const joint_score = [90,88,86,76,93,97,88,80,86,69,65,76,84];  
  // const data = JSON.parse(localStorage.getItem('obj'));
    //data의 값에는 const data = {"타임스코어" : [100, 90, 80 .... 80], "조인트스코어?" : [100,100,20,30,...50]}

  return (
    <ChartWrapper>
      <ChartContainer>
        <div className='chartContainer' style={{ display: "flex", justifyContent: "center", alignItems: "center", height: "100vh" }}>
            <div style={{ display: "flex", justifyContent: "space-around" }} className='ChartBox'>
                <Chart rate={time_score} />    {/*여기는 타임 스코어 데이터만 넘겨주면 됩니다. */}
                <Chart2 rate={joint_score} />  {/*여기는 조인트 스코어 데이터만 넘겨주면 됩니다. */}
            </div>
        </div>
      </ChartContainer>
    </ChartWrapper>
  );

}

export default ResultChart;