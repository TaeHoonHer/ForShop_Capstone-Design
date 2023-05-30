import Main from '../Pages/Main';
import IntroLoad from '../Pages/IntroLoad';
import Best from '../Pages/Best';
import Login from '../Pages/Login';
import Upload from '../Pages/Upload';
import UpImage from '../Pages/UpImage';
import AnVideo from '../Pages/AnVideo';
import MainVideo from '../Pages/MainVideo';
import ImageBoard from '../Pages/ImageBoard';
import videoBoard from '../Pages/videoBoard';
import AnVideoResult from '../Pages/AnVideoResult';
import Mypage from '../Pages/Mypage';

const routes = [
    {
        path: '/',
        component: Best
    },
    {
        path: '/IntroimgMain',
        component: IntroLoad
    },
    {
        path: '/main',
        component: Main
    },
    {
        path: '/login',
        component: Login
    },
    {
        path: '/upload',
        component: Upload
    },
    {
        path: '/upimg',
        component: UpImage
    },
    {
        path: '/anvideo',
        component: AnVideo
    },
    {
        path: '/anvideo/result',
        component: AnVideoResult
    },
    {
        path: '/mainvideo',
        component: MainVideo
    },
    {
        path: '/main/imgboard',
        component: ImageBoard
    },
    {
        path: '/main/vdboard',
        component: videoBoard
    },
    {
        path: '/mypage',
        component: Mypage
    }
];

export default routes;







