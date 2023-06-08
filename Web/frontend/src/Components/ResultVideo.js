import React, { useState, useEffect } from 'react';
import { useSelector } from 'react-redux';
import '../Css/ResultVideo.css';

function ResultVideo () {
    const data = JSON.parse(localStorage.getItem('obj'));
    const [score, setScore] = useState(0);
    const targetScore = 86;
    const scoreImg = [
        {src: '/img/res1.gif', alt: '75 ~ 100', level: 4},
        {src: '/img/res2.gif', alt: '50 ~ 75', level: 3},
        {src: '/img/res3.gif', alt: '25 ~ 50', level: 2},
        {src: '/img/res4.gif', alt: '0 ~ 25', level: 1},
    ];

    useEffect(() => {
        const interval = setInterval(() => {
            setScore((prevScore) => prevScore + 10 > targetScore ? targetScore : prevScore + 1);
        }, 100);

        return () => clearInterval(interval);
    }, []); 

    let displayImg = scoreImg.find(image => {
        return score <= image.level * 25 && score > (image.level - 1) * 25;
    });

    if (!displayImg) {
        displayImg = scoreImg[scoreImg.length - 1];
    }

    return (
        <div className='RSVideoWrapper'>
            <div className='RSVideoContainer'>
                <div className='RSVideoTitle'>
                    <p>Result of Analysis</p>
                </div>
                <div className='RSVideoBox'>
                <div className='RSVideo'>
                    <video className='RSVideo' controls loop muted autoPlay>
                        <source src={"https://forshop-bucket.s3.ap-northeast-2.amazonaws.com/55bfcdeb-770f-4873-8de1-5a196873c3d5.mp4"} className='rsvideo'/>
                    </video>
                </div>
                <div className='RSVideo'>
                    <video className='RSVideo' controls loop muted autoPlay>
                    <source src={"https://forshop-bucket.s3.ap-northeast-2.amazonaws.com/543b0c60-2ced-4d84-86e2-2902150382cd.mp4"}  className='rsvideo' />
                    </video>
                </div>

                </div>
                <div className='RSVideoPoint'>
                    <img src = {displayImg.src} alt={displayImg.alt}/>
                    <p>총점 : &nbsp;&nbsp;&nbsp;{score} score</p>
                </div>
            </div>
        </div>
    )
};

export default ResultVideo;
