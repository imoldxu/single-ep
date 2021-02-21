import { PageContainer } from "@ant-design/pro-layout"
import { Button, Card, Col, DatePicker, message, Row, Select, Space, Statistic } from "antd"
import Form from "antd/lib/form/Form"
import { statistics } from '@/services/ant-design-pro/bill';
import { MinusOutlined, PlusOutlined } from "@ant-design/icons";
import { useRef, useState } from "react";
import FormItem from "antd/lib/form/FormItem";
import moment from 'moment';

const { RangePicker } = DatePicker;
const { Option } = Select;

const layout = {
    labelCol: { span: 6 },
    wrapperCol: { span: 18 },
};

export default ()=>{

    const [ statisticsValue, setStatistic ] = useState({income:0, pay:0})
    const formRef = useRef()

    async function handleSubmit(values) {
        const hide =  message.loading("统计中")
        const { rangeTime, payway } = values;
        let query = {}
        if(rangeTime){
            query.startTime = rangeTime[0].format('yyyy-MM-DD') + " 00:00:00"
            query.endTime = rangeTime[1].format('yyyy-MM-DD') + " 23:59:59"
        }
        if( payway && payway !=0 ){
            query.payway = payway
        }
        
        try{
            const result = await statistics(query)
            setStatistic(result)
        }catch(e){
            message.error(e.message, 3)
        }finally{
            hide()
        }
    }
  
    const handleReset = () => {
      const { getFieldsValue, setFieldsValue } = formRef.current
  
      const fields = getFieldsValue()
      for (let item in fields) {
        if ({}.hasOwnProperty.call(fields, item)) {
          if (fields[item] instanceof Array) {
            fields[item] = []
          } else {
            fields[item] = undefined
          }
        }
      }
      setFieldsValue(fields)
      handleSubmit(fields)
    }

    return (
        <PageContainer>
            <Space direction="vertical" style={{width:"100%"}}>
            <Card>
                <Form
                    {...layout}  
                    ref={formRef}
                    name="query"
                    initialValues={{}}
                    onFinish={handleSubmit}
                    onReset={handleReset}
                >
                    <Row gutter={24}>
                        <Col span={8}>
                            <FormItem name="rangeTime" label="时间段" >
                                <RangePicker />
                            </FormItem>
                        </Col>
                        <Col span={8}>
                            <FormItem name="payway" label="收款渠道">
                            <Select>
                                <Option value="0">全部</Option>
                                <Option value="1">微信</Option>
                                <Option value="2">支付宝</Option>
                                <Option value="3">市医保</Option>
                                <Option value="4">省医保</Option>
                                <Option value="5">现金</Option>
                            </Select>
                            </FormItem>
                        </Col>
                        <Col span={8}>
                            <Space>
                            <Button
                                type="primary"
                                className="margin-right"
                                htmlType="submit">
                                    确定
                                </Button>
                                <Button
                                htmlType="reset" >
                                    重置
                                </Button>
                            </Space>
                        </Col>
                    </Row>
                </Form>
            </Card>
            <Row gutter={16}>
                <Col span={8}>
                    <Card title="最终收入" bordered={false}>
                        <span style={{color:"green"}}>{(statisticsValue.income-statisticsValue.pay)/100}元</span>
                        
                    </Card>
                </Col>
                <Col span={8}>
                    <Card title="缴费收入" bordered={false}>
                        <span style={{color:"blue"}}>{statisticsValue.income/100}元</span>
                        
                    </Card>
                </Col>
                <Col span={8}>
                    <Card title="退款支出" bordered={false}>
                        <span style={{color:"red"}}>{statisticsValue.pay/100}元</span>
                    </Card>
                </Col>
            </Row>
            </Space>
        </PageContainer>
    )
}