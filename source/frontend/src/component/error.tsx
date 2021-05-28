import { FrownTwoTone } from "@ant-design/icons";
import { Typography } from "antd";
import React from "react";

const Error = ({message}: ErrorProps) => (
    <div className="error-container">
        <FrownTwoTone style={{fontSize: 32}} />
        <Typography.Text style={{marginTop: 10, fontSize: 16}} type="secondary">{message}</Typography.Text>
    </div>
)

type ErrorProps = {
    message: String
}

export default Error;