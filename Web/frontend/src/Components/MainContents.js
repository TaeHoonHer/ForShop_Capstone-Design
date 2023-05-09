import '../Css/MainContents.css';
import { useEffect, useRef } from 'react';

function MainContents() {
    const conimages = [
        { src: '/img/a1.jpg', alt: 'image1' },
        { src: '/img/a2.jpg', alt: 'image2' },
        { src: '/img/a3.jpg', alt: 'image3' },
        { src: '/img/a4.jpg', alt: 'image4' },
        { src: '/img/a5.jpg', alt: 'image5' },
        { src: '/img/a6.jpg', alt: 'image6' },
        { src: '/img/a7.jpg', alt: 'image7' },
        { src: '/img/a8.jpg', alt: 'image8' },
    ];

    const containerRef = useRef(null);

    useEffect(() => {
        const container = containerRef.current;
        const imageElems = container.querySelectorAll('img');

        imageElems.forEach((img, index) => {
            img.style.animationDelay = '${index * 100}ms';
            img.classList.add('fadeup');
        });
    }, []);

    return (
        <div className='MainContents'>
            <div className='ContentsContainer' ref={ containerRef }>
                {conimages.map((image, index) => (
                  <div className={`bg${index + 2}`} key={index}>
                    <img src={image.src} alt={image.alt} />
                  </div>
                ))}
            </div>
        </div>
    )
}

export default MainContents;