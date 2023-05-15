import '../Css/Best.css';
import React from 'react';
import { Link } from 'react-router-dom';
import IntroHeader from '../Components/IntroHeader';
import styled from 'styled-components';

const NextLink = styled(Link)`
      text-decoration : none;
`;

function Best() {

  const levelimages = [
    { src: '/img/a1.jpg', alt: 'image1' },
    { src: '/img/a2.jpg', alt: 'image2' },
    { src: '/img/a3.jpg', alt: 'image3' },
    { src: '/img/a4.jpg', alt: 'image4' },
    { src: '/img/a5.jpg', alt: 'image5' },
    { src: '/img/a6.jpg', alt: 'image6' },
    { src: '/img/a7.jpg', alt: 'image7' },
    { src: '/img/a8.jpg', alt: 'image8' },
  ];

  return (
    <div className="Intro">
      <IntroHeader/>
      <div className="intro_box">
        <div className='to_main'>
          <NextLink to="/"><p>Back</p></NextLink>
        </div>
        
        <div className="intro_container">
          <div className="img_container">
            {levelimages.map((image, index) => (
              <span className={`bg${index + 2}`} style={{'--i': index + 1}} key={index}>
                <img src={image.src} alt={image.alt} />
              </span>
            ))}
            <p className="intro_text">Best 8<br/>of<br/>For Shop</p>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Best;