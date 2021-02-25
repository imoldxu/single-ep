import { Button, Col, Descriptions, Divider, Input, message, Modal, Row, Select } from 'antd';
import Form from 'antd/lib/form/Form';
import FormItem from 'antd/lib/form/FormItem';
import React, { useRef, useState } from "react";

const { Option } = Select;

const layout = {
    labelCol: {
        span: 4,
    },
    wrapperCol: {
        span: 20,
    },
}

const UserModal = (props) => {
    const {
        handleCommit,
        handleCancel,
        visible,
        values,
    } = props;

    const formRef = useRef()
    const [isLoading, setLoading] = useState(false)

    const onOk = () => {
        formRef.current
            .validateFields()
            .then(async (values) => {

                if (handleCommit) {
                    setLoading(true)
                    try {
                        await handleCommit(values)
                        message.success('提交成功')
                    } catch(e){
                        message.error(e.message, 3)
                    } finally {
                        setLoading(false)
                    }
                }
            })
            .catch(info => {
                console.log('校验失败:', info);
            });
    }

    return (
        <Modal
            destroyOnClose={true}
            title="新建用户"
            visible={visible}
            onOk={onOk}
            confirmLoading={isLoading}
            onCancel={handleCancel}
        >
            <Form {...layout}
                ref={formRef}
                name="user"
                preserve={false}>
                <FormItem name="name" label="姓名"
                    rules={[{ required: true }]}
                    hasFeedback>
                    <Input placeholder="请输入用户姓名"></Input>
                </FormItem>
                <FormItem name="phone" label="手机号"
                    rules={[{ required: true }]}
                    hasFeedback
                >
                    <Input placeholder="请输入手机号" maxLength="11"></Input>
                </FormItem>
                <FormItem name="role" label="角色"
                    rules={[{ required: true }]}
                    hasFeedback>
                    <Select>
                        <Option value="manager">管理员</Option>
                        <Option value="tollman">收费员</Option>
                    </Select>
                </FormItem>
            </Form>
        </Modal>
    );
};

export default UserModal;
