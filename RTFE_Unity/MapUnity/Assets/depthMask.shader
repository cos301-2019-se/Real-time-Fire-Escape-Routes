Shader "Masked/Mask"
{
 
    SubShader
    {
        Tags{"Queue" = "Geometry-1"}
        
        Colormask 0
        ZWrite On
        
        Pass{}
    }
  
}
