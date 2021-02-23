import ProForm from "@ant-design/pro-form"
import { PageContainer } from "@ant-design/pro-layout"
import { Button, Card, Col, Input, message, Row } from "antd"
import Form from "antd/lib/form/Form"
import FormItem from "antd/lib/form/FormItem"
import React, { useRef } from "react"
import { modifyPassword } from '@/services/ant-design-pro/login';
import { history } from "umi"
import { CheckOutlined, RedoOutlined, RollbackOutlined } from "@ant-design/icons"
import MD5 from 'crypto-js/md5'

const layout = {
    labelCol: {
        span: 6,
    },
    wrapperCol: {
        span: 12,
    },
}

export default ()=>{

    const formRef = useRef()

    async function handleSubmit(values){
        const hide = message.loading('提交中')
        try{
            const payload ={
                oldPassword: MD5(values.oldPassword+"x").toString(),
                newPassword: MD5(values.newPassword+"x").toString()
            }

            await modifyPassword(payload)
            message.success('修改成功', 3)
            history.goBack()
        }catch(e){
            console.log(e)
            message.error(e.message, 3)
        }finally{
            hide()
        }
    } 

    return (
        <PageContainer
            extra={[
                <Button type="default" key="2" onClick={()=> history.goBack()}><RollbackOutlined /> 返回</Button>,
            ]}>
            <Card>
            <Form {...layout}
                ref={formRef}
                title="修改密码"
                onFinish={handleSubmit}
                name="refundDrug">
                <FormItem name="oldPassword" label="旧密码" 
                    rules={[{required:true}]} hasFeedback>
                    <Input type="password" placeholder="请输入旧密码"></Input>
                </FormItem>
                <FormItem name="newPassword" label="新密码" 
                    rules={[{required:true}]} hasFeedback>
                    <Input type="password" placeholder="请输入新密码"></Input>
                </FormItem>
                <Row gutter={24} justify="center">
                    <Col span={3}><Button type="default" htmlType="reset">重置</Button></Col>
                    <Col span={3}><Button type="primary" htmlType="submit">提交</Button></Col>
                </Row>   
            </Form>
            </Card>
        </PageContainer>
    )
}