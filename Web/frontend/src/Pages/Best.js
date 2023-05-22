import '../Css/Best.css';
import React, { useState, useEffect, useRef } from 'react';
import { Link } from 'react-router-dom';
import IntroHeader from '../Components/IntroHeader';
import styled from 'styled-components';

const NextLink = styled(Link)`
  text-decoration: none;
`;

function Best() {
  const levelvideos = [
    { src: '/img/new1.mp4', alt: 'video1' },
    { src: '/img/new2.mp4', alt: 'video2' },
    { src: '/img/new1.mp4', alt: 'video1' },
    { src: '/img/new2.mp4', alt: 'video2' },
    { src: '/img/new1.mp4', alt: 'video1' },
    { src: '/img/new2.mp4', alt: 'video2' },
    
  ];

  const [currentIndex, setCurrentIndex] = useState(0);
  const [animate, setAnimate] = useState(false);

  const videoRefs = useRef(levelvideos.map(() => React.createRef()));

  const rotateLeft = () => {
    setCurrentIndex((prevIndex) =>
      prevIndex === 0 ? levelvideos.length - 1 : prevIndex - 1
    );
  };

  const rotateRight = () => {
    setCurrentIndex((prevIndex) =>
      prevIndex === levelvideos.length - 1 ? 0 : prevIndex + 1
    );
  };

  useEffect(() => {
    const playCurrentVideo = () => {
      const currentVideoRef = videoRefs.current[currentIndex];
      if (currentVideoRef && currentVideoRef.current) {
        currentVideoRef.current.play();
      }
    };
  
    videoRefs.current.forEach((ref, index) => {
      if (index === currentIndex) {
        ref.current.play();
      } else {
        ref.current.pause();
        ref.current.currentTime = 0; 
      }
    });
  
    playCurrentVideo();
  }, [currentIndex, videoRefs]);

  return (
    <div className="Intro">
      <IntroHeader />
      <div className="intro_box">
        <h2 className="Intro_Title">Service &nbsp;&nbsp;&nbsp;Demo</h2>
        <button className="to_main">
          <NextLink to="/anvideo">
            <p>Try It!</p>
          </NextLink>
        </button>

        <div className="intro_container">
          <button className="intro_leftBtn" onClick={rotateLeft}>
            <img src="/img/left-chevron.png" alt="Left" />
          </button>
          <button className="intro_rightBtn" onClick={rotateRight}>
            <img src="/img/right-chevron.png" alt="Right" />
          </button>
          <div className={`img_container ${animate ? 'animate' : ''}`}>
          {levelvideos.map((video, index) => (
            <span
              className={`bg${index + 2}`}
              style={{ '--i': index - currentIndex }}
              key={index}
            >
              <video
                ref={videoRefs.current[index]}
                src={video.src}
                alt={video.alt}
                controls
                muted
                loop
                style={{ objectFit: 'contain' }}
                className={currentIndex === index ? "active" : "inactive"}
                onCanPlayThrough={event => {
                  if (currentIndex === index) {
                    event.target.play();
                  }
                }}
              />
            </span>
          ))}
          
          </div>
        </div>
      </div>
    </div>
  );
}

export default Best;