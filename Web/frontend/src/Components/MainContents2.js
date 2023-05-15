import '../Css/MainContents.css';
import { useEffect, useRef } from 'react';

function MainContents2() {
    const convideos = [
        { src: '/img/dance.mp4', alt: 'video1' },
        { src: '/img/intro3.mp4', alt: 'video2' },
    ];

    const containerRef = useRef(null);

    useEffect(() => {
        const container = containerRef.current;
        const videoElems = container.querySelectorAll('video');

        videoElems.forEach((video, index) => {
            video.style.animationDelay = '${index * 100}ms';
            video.classList.add('fadeup');
        });
    }, []);

    return (
        <div className='MainContents'>
            <div className='ContentsContainer' ref={ containerRef }>
                {convideos.map((video, index) => (
                  <div className={`bg${index + 2}`} key={index}>
                    <video src={video.src} alt={video.alt} autoPlay loop muted />
                  </div>
                ))}
            </div>
        </div>
    )
}

export default MainContents2;