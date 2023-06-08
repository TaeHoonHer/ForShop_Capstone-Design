import React from 'react';
import styled from 'styled-components';
import '../Css/AnVideoResult.css';
import DetailHeader from '../Components/DetailHeader';
import ResultVideo from '../Components/ResultVideo';
import ResultChart from '../Components/ResultChart';
import Footer from '../Components/Footer';
import { useNavigate } from 'react-router-dom';
import '../Css/AnVideoResult.css';

const ResultWrapper = styled.div`
  width : 100vw;
  min-height : 100vh;
  overflow-x : hidden;
  padding : 0;
  margin : 0;
  box-sizing : border-box;
`;

function AnVideoResult () {
  
  const navigate = useNavigate();

  const handleAgain = () => {
    navigate('/anvideo');
  };
  
  return (
    <ResultWrapper>
      <DetailHeader/>
      <ResultVideo/>
      <ResultChart/>
      <button type='button' className='againAnvideo' onClick={handleAgain}>Again?</button>
      <Footer/>
    </ResultWrapper>
  );
};

export default AnVideoResult;