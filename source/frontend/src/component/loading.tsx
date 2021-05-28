import { LoadingOutlined } from "@ant-design/icons";
import React from "react";

const Loading = () => (
    <div className="error-container">
        <LoadingOutlined style={{fontSize: 48, marginBottom: 10}} />
        Chargement. Veuillez patienter...
    </div>
)

export default Loading;