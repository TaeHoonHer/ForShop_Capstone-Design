import '../Css/MainContents2.css';
import { useEffect, useRef, useState } from 'react';
import { Link } from 'react-router-dom';

function MainContents2() {
    const [visibleVideos, setVisibleVideos] = useState(8);
    const convideos = [
        { src: '/img/dance.mp4', id: 1, title: 'video1', content: '하이요1' },
        { src: '/img/intro3.mp4', id: 2, title: 'video2', content: '하이요2' },
        { src: '/img/banner.mp4', id: 3, title: 'video3', content: '하이요3' },
        { src: '/img/new1.mp4', id: 4, title: 'video4', content: '하이요4' },
        { src: '/img/new2.mp4', id: 5, title: 'video5', content: '하이요5' },
        { src: '/img/v1.mp4', id: 6, title: 'video6', content: '하이요6' },
        { src: '/img/v2.mp4', id: 7, title: 'video7', content: '하이요7' },
        { src: '/img/v3.mp4', id: 8, title: 'video8', content: '하이요8' },
        { src: '/img/v4.mp4', id: 9, title: 'video9', content: '하이요9' },
        { src: '/img/v5.mp4', id: 10, title: 'video10', content: '하이요10' },
    ];

    const handleShowMore = () => {
        setVisibleVideos(prevVisibleVideos => prevVisibleVideos + 8);
    };

    const containerRef = useRef(null);

    useEffect(() => {
        const container = containerRef.current;
        const videoElems = container.querySelectorAll('video');

        videoElems.forEach((video, index) => {
            video.style.animationDelay = '${index * 100}ms';
            video.classList.add('fadeup');
        });
    }, [visibleVideos]);

    return (
        <div className='MainContents'>
            <div className='ContentsContainer' ref={ containerRef }>
                {convideos.slice(0, visibleVideos).map((video, index) => (
                  <div className={`bg${index + 2}`} key={index}>
                    <Link to={`/main/vdboard/?id=${video.id}&title=${video.title}&src=${video.src}&content=${video.content}`}>
                        <video src={video.src} alt={video.alt} autoPlay loop muted />
                    </Link>
                  </div>
                ))}
            </div>
            {visibleVideos < convideos.length && (
                <div className='ButtonContainer'>
                  <button className="ShowMoreButton" onClick={handleShowMore}>
                    더 보기
                  </button>
                </div>
            )}
        </div>
    )
}

export default MainContents2;