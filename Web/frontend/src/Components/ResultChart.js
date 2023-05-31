import React, { useState } from 'react';
import styled from 'styled-components';
import '../Css/ResultChart.css';
import {Chart, Chart2} from './Chart';


const ChartWrapper = styled.div`
    position : relative;
    width : 100vw;
    height : 50vh;
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

function ResultChart ({data}) {
    
    //data의 값에는 const data = {"타임스코어" : [100, 90, 80 .... 80], "조인트스코어?" : [100,100,20,30,...50]}
  console.log(data);
  return (
    <ChartWrapper>
      <ChartContainer>
        <div className='chartContainer' style={{ display: "flex", justifyContent: "center", alignItems: "center", height: "100vh" }}>
            <div style={{ display: "flex", justifyContent: "space-around", width: "80%" }}>
                <Chart rate={data.time_score} />    {/*여기는 타임 스코어 데이터만 넘겨주면 됩니다. */}
                <Chart2 rate={data.joint_score} />  {/*여기는 조인트 스코어 데이터만 넘겨주면 됩니다. */}
            </div>
        </div>
      </ChartContainer>
    </ChartWrapper>
  );

}

export default ResultChart;