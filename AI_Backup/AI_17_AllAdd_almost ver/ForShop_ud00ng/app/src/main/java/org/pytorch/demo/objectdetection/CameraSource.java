package org.pytorch.demo.objectdetection;

//카메라, 전면, 후면 전환을 위한 Class

public class CameraSource {
    public enum CameraFacing {
        FRONT,
        BACK
    }

    private CameraFacing cameraFacing = CameraFacing.BACK;

    public CameraFacing getCameraFacing() {
        return cameraFacing;
    }

    public void setCameraFacing(CameraFacing cameraFacing) {
        this.cameraFacing = cameraFacing;
    }

}
