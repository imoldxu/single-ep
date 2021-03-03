import { checkTwoPointNum, regFenToYuan, regYuanToFen } from '@/utils/money';
import { Button, Col, Descriptions, Divider, Input, message, Modal, Row, Select, Space, Statistic } from 'antd';
import Form from 'antd/lib/form/Form';
import FormItem from 'antd/lib/form/FormItem';
import Text from 'antd/lib/typography/Text';
import React, { useRef, useState } from "react";

const PayModal = (props) => {
    const {
        //handleCashPay,
        handleYibaoPay,
        handleYidiYibaoPay,
        handleCancel,
        visible,
        values,
    } = props;

    const {orderno="", amount} =values

    return (
        <Modal
            destroyOnClose={true}
            title="收款确认"
            visible={visible}
            //onOk={onOk}
            onCancel={handleCancel}
            footer={[
                <Button key="back" onClick={handleCancel}>
                  关闭
                </Button>,
            ]}
        >
            <Space direction="vertical" size="small" style={{width:"100%"}}>
                <Row key="header" justify="center" gutter={[16,16]}>
                    <Statistic key="amount" title="缴费金额" value={`${regFenToYuan(amount)}元`} valueStyle={{color:"red"}}/>     
                </Row>
                <Row key="tip" justify="center" gutter={[16,16]}>
                    <span key="ps">请确认在已经收到患者缴费的情况下，选择以下收费方式</span>
                </Row>
                <Row key="payway" justify="center" gutter={[16,16]}>    
                    <Space key="buttons" direction="vertical" align="center" size="large">
                        {/* <Button key="cash" style={{backgroundColor:"#1ca4e4", width:"300px", color:"white"}} onClick={()=>handleCashPay(orderno)} size="large" shape="round">现 金</Button> */}
                        <Button key="shiyibao" style={{backgroundColor:"#d2960f", width:"300px", color:"white"}} onClick={()=>handleYibaoPay(orderno)} size="large" shape="round">市 医 保</Button>
                        <Button key="yidiyibao" style={{backgroundColor:"#9a9e9a", width:"300px", color:"white"}} onClick={()=>handleYidiYibaoPay(orderno)} size="large" shape="round">异 地 医 保</Button>
                    </Space>
                </Row>
            </Space>
        </Modal>
    );
};

export default PayModal;
