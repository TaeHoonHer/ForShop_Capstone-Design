import '../Css/Banner.css'

function Banner() {
    return (
        <div className="Banner">
            <video autoPlay muted loop>
                <source src="../img/banner.mp4" type="video/mp4"></source>
            </video>
            <h2>4Shop</h2>
            <div className='search'>
                <img src="../img/search.png"/>
                <input className='searchIn' placeholder='키워드, ID 검색'></input>
            </div>
        </div>
    );
}

export default Banner;