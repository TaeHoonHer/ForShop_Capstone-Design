import React from 'react';
import styled from 'styled-components';
import '../Css/AnVideoResult.css';
import DetailHeader from '../Components/DetailHeader';
import ResultVideo from '../Components/ResultVideo';
import ResultChart from '../Components/ResultChart';
import Footer from '../Components/Footer';

const ResultWrapper = styled.div`
  width : 100vw;
  min-height : 100vh;
  overflow-x : hidden;
  padding : 0;
  margin : 0;
  box-sizing : border-box;
`;

function AnVideoResult () {
  return (
    <ResultWrapper>
      <DetailHeader/>
      <ResultVideo/>
      <ResultChart/>
      <Footer/>
    </ResultWrapper>
  );
};

export default AnVideoResult;