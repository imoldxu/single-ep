import { checkTwoPointNum, regFenToYuan, regYuanToFen } from '@/utils/money';
import { Button, Col, Descriptions, Divider, Input, message, Modal, Row, Select } from 'antd';
import Form from 'antd/lib/form/Form';
import FormItem from 'antd/lib/form/FormItem';
import React, { useRef, useState } from "react";

const { Option } = Select;

const layout = {
    labelCol: {
        span: 8,
    },
    wrapperCol: {
        span: 16,
    },
}

const DrugModal = (props) => {
    const {
        handleCommit,
        handleInfoModalVisible,
        visible,
        values,
    } = props;

    const formRef = useRef()
    const [isLoading, setLoading] = useState(false)

    const { id, price=0 } = values;
    const priceYuan = regFenToYuan(price)

    const title = id ? "修改药品" : "新建药品"

    const onOk = () => {

        formRef.current
            .validateFields()
            .then(values => {

                values.price = regYuanToFen(values.price, 100)

                if (handleCommit) {
                    setLoading(true)
                    try{
                        handleCommit(values)
                    } catch(e){
                        message.error(e.message, 3)
                    } finally{
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
            width={800}
            destroyOnClose={true}
            title={title}
            visible={visible}
            onOk={onOk}
            confirmLoading={isLoading}
            onCancel={handleInfoModalVisible}
        >
            <Form {...layout}
                ref={formRef}
                name="drug"
                initialValues={{ ...values, price: priceYuan }}
                preserve={false}>
                {
                    id ? (<FormItem name="id" noStyle>
                        <Input type="hidden"></Input>
                    </FormItem>) : ('')
                }
                <Row gutter={16}>
                    <Col span={12}>
                        <FormItem name="drugname" label="药品名称"
                            rules={[{ required: true }]}
                            hasFeedback
                        >
                            <Input placeholder="药品名称"></Input>
                        </FormItem>
                    </Col>
                    <Col span={12}>
                        <FormItem name="shortname" label="药品简称"
                            rules={[{ required: true }]}
                            hasFeedback>
                            <Input placeholder="药品简称"></Input>
                        </FormItem>
                    </Col>

                </Row>
                {
                    id ? (
                        <Row gutter={16}>
                            <Col span={12}>
                                <FormItem name="fullkeys" label="全称拼音缩写"
                                    rules={[{ required: true }]}
                                    hasFeedback
                                >
                                    <Input placeholder="药品名称"></Input>
                                </FormItem>
                            </Col>
                            <Col span={12}>
                                <FormItem name="shortnamekeys" label="简称拼音缩写"
                                    rules={[{ required: true }]}
                                    hasFeedback
                                >
                                    <Input placeholder="药品名称"></Input>
                                </FormItem>
                            </Col>
                        </Row>
                    ) : ('')
                }
                <Row gutter={16}>
                    <Col span={12}>
                        <FormItem name="standard" label="规格"
                            rules={[{ required: true }]}
                            hasFeedback>
                            <Input placeholder="规格"></Input>
                        </FormItem>
                    </Col>
                    <Col span={12}>
                        <FormItem name="form" label="剂型"
                            rules={[{ required: true }]}
                            hasFeedback>
                            <Select>
                                <Option value="搽剂">搽剂</Option>
                                <Option value="肠溶片">肠溶片</Option>
                                <Option value="滴剂">滴剂</Option>
                                <Option value="滴丸剂">滴丸剂</Option>
                                <Option value="滴眼液">滴眼液</Option>
                                <Option value="分散片">分散片</Option>
                                <Option value="粉剂">粉剂</Option>
                                <Option value="粉针剂">粉针剂</Option>
                                <Option value="干混悬剂">干混悬剂</Option>
                                <Option value="膏剂">膏剂</Option>
                                <Option value="膏药">膏药</Option>
                                <Option value="含漱液">含漱液</Option>
                                <Option value="合剂">合剂</Option>
                                <Option value="糊剂">糊剂</Option>
                                <Option value="混悬剂">混悬剂</Option>
                                <Option value="胶囊剂">胶囊剂</Option>
                                <Option value="酒剂">酒剂</Option>
                                <Option value="颗粒剂">颗粒剂</Option>
                                <Option value="口服溶液">口服溶液</Option>
                                <Option value="口服液">口服液</Option>
                                <Option value="凝胶剂">凝胶剂</Option>
                                <Option value="浓缩丸">浓缩丸</Option>
                                <Option value="喷剂">喷剂</Option>
                                <Option value="喷雾剂">喷雾剂</Option>
                                <Option value="喷雾剂(鼻用)">喷雾剂(鼻用)</Option>
                                <Option value="片剂">片剂</Option>
                                <Option value="片剂(薄膜衣)">片剂(薄膜衣)</Option>
                                <Option value="气雾剂">气雾剂</Option>
                                <Option value="器械">器械</Option>
                                <Option value="器械(二类)">器械(二类)</Option>
                                <Option value="溶液">溶液</Option>
                                <Option value="溶液剂">溶液剂</Option>
                                <Option value="软膏剂">软膏剂</Option>
                                <Option value="软胶囊">软胶囊</Option>
                                <Option value="散剂">散剂</Option>
                                <Option value="栓">栓</Option>
                                <Option value="栓剂">栓剂</Option>
                                <Option value="糖浆剂">糖浆剂</Option>
                                <Option value="涂剂">涂剂</Option>
                                <Option value="外用">外用</Option>
                                <Option value="丸剂">丸剂</Option>
                                <Option value="洗液">洗液</Option>
                                <Option value="硬胶囊">硬胶囊</Option>
                                <Option value="中药饮片">中药饮片</Option>
                                <Option value="注射剂">注射剂</Option>
                                <Option value="注射液">注射液</Option>
                            </Select>
                        </FormItem>
                    </Col>
                </Row>
                <Row gutter={16}>
                    <Col span={12}>
                        <FormItem name="category" label="类别"
                            rules={[{ required: true }]}
                            hasFeedback>
                            <Select>
                                <Option value="OTC">OTC</Option>
                                <Option value="处方药">处方药</Option>
                                <Option value="耗材">耗材</Option>
                                <Option value="特殊膳食">特殊膳食</Option>
                                <Option value="保健品">保健品</Option>
                                <Option value="保健食品">保健食品</Option>
                                <Option value="营养膳食">营养膳食</Option>
                                <Option value="消毒用品">消毒用品</Option>
                                <Option value="饮片">饮片</Option>
                                <Option value="外用">外用</Option>
                                <Option value="中药">中药</Option>
                            </Select>

                        </FormItem>
                    </Col>
                    <Col span={12}>
                        <FormItem name="subcategory" label="子类"
                            rules={[{ required: true }]}
                            hasFeedback>
                            <Select>
                                <Option value="抗生素">抗生素</Option>
                                <Option value="输液">输液</Option>
                                <Option value="心血管">心血管</Option>
                                <Option value="呼吸">呼吸</Option>
                                <Option value="消化">消化</Option>
                                <Option value="神经">神经</Option>
                                <Option value="内分泌">内分泌</Option>
                                <Option value="解热镇痛">解热镇痛</Option>
                                <Option value="维生素激素">维生素激素</Option>
                                <Option value="外用">外用</Option>
                                <Option value="中成药">中成药</Option>
                                <Option value="滋补">滋补</Option>
                            </Select>
                        </FormItem>
                    </Col>
                </Row>
                <Row gutter={16}>
                    <Col span={12}>
                        <FormItem name="price" label="销售单价(元)"
                            rules={[{ required: true, validator: (_, value) => 
                                checkTwoPointNum(value)?
                                    Promise.resolve() :  Promise.reject('请输入正确的金额')
                            }]}
                            hasFeedback>
                            <Input placeholder="请输入单价"></Input>
                        </FormItem>
                    </Col>
                    <Col span={12}>
                        <FormItem name="unit" label="销售单位"
                            rules={[{ required: true }]}
                            hasFeedback>
                            <Select>
                                <Option value="盒">盒</Option>
                                <Option value="袋">袋</Option>
                                <Option value="支">支</Option>
                                <Option value="瓶">瓶</Option>
                                <Option value="g">g</Option>
                                <Option value="kg">kg</Option>
                            </Select>
                        </FormItem>
                    </Col>
                </Row>
                <Row gutter={16}>
                    <Col span={12}>
                    <FormItem name="company" label="厂家"
                        rules={[{ required: true }]}
                        hasFeedback>
                        <Input placeholder="请输入药品生成厂家"></Input>
                    </FormItem>
                    </Col>
                </Row>
                <Divider>医生开方默认设置</Divider>
                <Row gutter={16}>
                    <Col span={8}>
                        <FormItem name="singledose" label="单次剂量"
                            rules={[{ required: true }]}
                            hasFeedback>
                            <Input placeholder="请输入默认的单次剂量"></Input>
                        </FormItem>
                    </Col>
                    <Col span={8}>
                        <FormItem name="frequency" label="频次"
                            rules={[{ required: true }]}
                            hasFeedback>
                            <Input placeholder="请输入默认的使用频次"></Input>
                        </FormItem>
                    </Col>
                    <Col span={8}>
                        <FormItem name="defaultusage" label="默认用法"
                            rules={[{ required: true }]}
                            hasFeedback>
                            <Input placeholder="请输入默认的使用方法"></Input>
                        </FormItem>
                    </Col>
                </Row>




            </Form>
        </Modal>
    );
};

export default DrugModal;
