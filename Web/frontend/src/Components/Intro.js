import * as THREE from 'three'
import { useRef, useState } from 'react'
import { Canvas, createPortal, useFrame, useThree } from '@react-three/fiber'
import { useFBO, useGLTF, useScroll, Text, Image, Scroll, Preload, ScrollControls, MeshTransmissionMaterial, Html } from '@react-three/drei'
import { easing } from 'maath'
import { useNavigate } from 'react-router-dom'
import '../Css/Intro.css'

export default function Intro() {
  return (
    <Canvas style={{ height: "100vh", width: "100vw" }} camera={{ position: [0, 0, 20], fov: 15 }}>
      <ScrollControls damping={0.2} pages={3} distance={0.5}>
        <Lens>
          <Scroll>
            <Typography />
            <Images />
          </Scroll>
          <Scroll html>
            <div style={{ transform: 'translate3d(65vw, 192vh, 0)' }}>
              Image Contents Platform Service
              <br />
              RealTime-Guideline
              <br />
              Video Motion Analysis
              <br />
            </div>
          </Scroll>
          <Preload />
        </Lens>
      </ScrollControls>
    </Canvas>
  )
}

function Lens({ children, damping = 0.15, ...props }) {
  const ref = useRef()
  const { nodes } = useGLTF('./img/lens-transformed.glb')
  const cylinderGeometry = nodes.Cylinder?.geometry // Cylinder 모델의 geometry 가져오기
  const buffer = useFBO()
  const viewport = useThree((state) => state.viewport)
  const [scene] = useState(() => new THREE.Scene())
  useFrame((state, delta) => {

    const viewport = state.viewport.getCurrentViewport(state.camera, [0, 0, 15])
    easing.damp3(
      ref.current.position,
      [(state.pointer.x * viewport.width) / 2, (state.pointer.y * viewport.height) / 2, 15],
      damping,
      delta
    )

    state.gl.setRenderTarget(buffer)
    state.gl.setClearColor('#000000')
    state.gl.render(scene, state.camera)
    state.gl.setRenderTarget(null)
  })
  return (
    <>
      {createPortal(children, scene)}
      <mesh scale={[viewport.width, viewport.height, 1]}>
        <planeGeometry />
        <meshBasicMaterial map={buffer.texture} />
      </mesh>
      <mesh scale={0.25} ref={ref} rotation-x={Math.PI / 2} geometry={cylinderGeometry} {...props}>
        <MeshTransmissionMaterial buffer={buffer.texture} ior={1.2} thickness={1.5} anisotropy={0.1} chromaticAberration={0.04} />
      </mesh>
    </>
  )
}

function Images() {
  const navigate = useNavigate()
  const group = useRef()
  const data = useScroll()
  const { width, height } = useThree((state) => state.viewport)

  useFrame(() => {
    group.current.children[0].material.zoom = 1 + data.range(0, 1 / 3) / 3
    group.current.children[1].material.zoom = 1 + data.range(0, 1 / 3) / 3
    group.current.children[2].material.zoom = 1 + data.range(1.15 / 3, 1 / 3) / 2
    group.current.children[3].material.zoom = 1 + data.range(1.15 / 3, 1 / 3) / 2
    group.current.children[4].material.zoom = 1 + data.range(1.15 / 3, 1 / 3) / 2
    group.current.children[5].material.grayscale = 1 - data.range(1.6 / 3, 1 / 3)
    group.current.children[6].material.zoom = 1 + (1 - data.range(2 / 3, 1 / 3)) / 3
  })

  const handleButtonClick = () => {
    navigate('/upimg');
  }

  return (
    <group ref={group}>
      <Image position={[-2, 0, 0]} scale={[4, height, 1]} url="/img/cat.jpg" />
      <Image position={[2, 0, 3]} scale={3} url="/img/couple.jpg" />
      <Image position={[-2.05, -height, 6]} scale={[1, 3, 1]} url="/img/b2.jpg" />
      <Image position={[-0.6, -height, 9]} scale={[1, 2, 1]} url="/img/a8.jpg" />
      <Image position={[0.75, -height, 10.5]} scale={1.5} url="/img/a5.jpg"/>
      <Image position={[0, -height * 1.5, 7.5]} scale={[1.5, 3, 1]} url="/img/a3.jpg" />
      <Image position={[0, -height * 2 - height / 4, 0]} scale={[width, height / 1.1, 1]} url="/img/a7.jpg" />
      <mesh position={[0, -height * 2 - height / 4, 0.1]} scale={[width, height / 1.1, 1]}>
        <Html center scaleFactor={20}>
          <button class = "to_mainBtn" 
            style={{ 
              zIndex: 1, 
              position: 'relative', 
              width: '200px', 
              height: '80px', 
              fontSize: '25px', 
              borderRadius: '20px', 
              cursor: 'pointer', 
              border: 'none',
              backgroundColor: '#f386fd',
              color: 'white', 
              boxShadow: '0px 2px 4px #888' 
            }} onClick={handleButtonClick}>
            Let's Start!
          </button>
        </Html>
      </mesh>
    </group>
  )
}

function Typography() {
  const state = useThree()
  const { width, height } = state.viewport.getCurrentViewport(state.camera, [0, 0, 12])
  const shared = { font: 'https://fonts.googleapis.com/css2?family=Poppins:wght@700&display=swap', letterSpacing: -0.1, color: 'white' }
  return (
    <>
      <Text children="Guide" anchorX="left" position={[-width / 2.5, -height / 10, 12]} {...shared} />
      <Text children="Analysis" anchorX="right" position={[width / 2.5, -height * 2, 12]} {...shared} />
      <Text children="Rate" position={[0, -height * 4.624, 12]} {...shared} />
    </>
  )
}
