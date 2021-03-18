import { PageContainer } from "@ant-design/pro-layout"
import { Button, Card, Col, DatePicker, message, Row, Select, Space, Statistic, Table } from "antd"
import Form from "antd/lib/form/Form"
import { downloadMyBill, mystatistics, queryMyBill } from '@/services/ant-design-pro/bill';
import { DownloadOutlined, MinusOutlined, PlusOutlined } from "@ant-design/icons";
import { useRef, useState } from "react";
import FormItem from "antd/lib/form/FormItem";
import moment from 'moment';
import { regFenToYuan } from "@/utils/money";
import ProTable from "@ant-design/pro-table";

const { RangePicker } = DatePicker;
const { Option } = Select;

const layout = {
    labelCol: { span: 6 },
    wrapperCol: { span: 18 },
};

export default ()=>{

    const [ statisticsValue, setStatistic ] = useState({income:0, pay:0})
    // const [ tableData, setTableData] = useState({current:1, pageSize:20, total:0, data:[]})
    const formRef = useRef()
    const actionRef = useRef()

    async function handleSubmit(values) {
        const hide =  message.loading("统计中")
        const { rangeTime, payway } = values;
        let query = {}
        if(rangeTime && rangeTime.length>1){
            query.startTime = rangeTime[0].format('yyyy-MM-DD') + " 00:00:00"
            query.endTime = rangeTime[1].format('yyyy-MM-DD') + " 23:59:59"
        }
        if( payway && payway !=0 ){
            query.payway = payway
        }
        
        try{
            const result = await mystatistics(query)
            setStatistic(result)
            actionRef.current.reloadAndRest()
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

    const columns = [
        {
            title: '支付方式',
            dataIndex: 'payway',
            search: false,
            valueEnum: {
                1: {
                    text: '微信',
                },
                2: {
                    text: '支付宝',
                }, 
                3: {
                    text: '市医保',
                },
                4: {
                    text: '异地医保',
                },
                5: {
                    text: '现金',
                },
                6: {
                  text: '银行卡',
                }
            }
        },
        {
            title: '支付流水号/收款员',
            dataIndex: 'payid',
            search: false,
            hideInTable: true,
        },
        {
            title: '账单金额',
            dataIndex: 'amount',
            search: false,
            render: (_,record)=> {
              if(record.type===1){  
                return (
                  <span style={{color:"green"}}>{regFenToYuan(record.amount)}元</span>
                  )
              }else{
                  return (
                      <span style={{color:"red"}}>{regFenToYuan(0-record.amount)}元</span>
                  )  
              }
              }
        },
        {
            title: '关联订单号',
            dataIndex: 'orderno',
            search: false,
        },
        {
          title: '账单时间',
          dataIndex: 'createtime',
          search: false,
        },
      ];


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
                            <FormItem name="rangeTime" label="时间段" rules={[{required: true}]}>
                                <RangePicker />
                            </FormItem>
                        </Col>
                        <Col span={8}>
                            <FormItem name="payway" label="收款渠道">
                            <Select>
                                <Option value="0">全部</Option>
                                <Option value="1">微信</Option>
                                <Option value="2">支付宝</Option>
                                <Option value="3">医保</Option>
                                <Option value="4">异地医保</Option>
                                <Option value="5">现金</Option>
                                <Option value="6">银行卡</Option>
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
                        <span style={{color:"green",fontSize:"50px"}}>{regFenToYuan(statisticsValue.income-statisticsValue.pay)}元</span>
                        
                    </Card>
                </Col>
                <Col span={8}>
                    <Card title="缴费收入" bordered={false}>
                        <span style={{color:"blue",fontSize:"50px"}}>{regFenToYuan(statisticsValue.income)}元</span>
                        
                    </Card>
                </Col>
                <Col span={8}>
                    <Card title="退款支出" bordered={false}>
                        <span style={{color:"red",fontSize:"50px"}}>{regFenToYuan(statisticsValue.pay)}元</span>
                    </Card>
                </Col>
            </Row>
            <Card>
                <ProTable
                    headerTitle="账单明细"
                    actionRef={actionRef}
                    rowKey="key"
                    request={async (params) => {
                        let values;
                        try{
                            values = await formRef.current.validateFields()
                        }catch(e){
                            return
                        }
                        try{
                            const { rangeTime, payway } = values;
                            let query = {}
                            if(rangeTime && rangeTime.length>1){
                                query.startTime = rangeTime[0].format('yyyy-MM-DD') + " 00:00:00"
                                query.endTime = rangeTime[1].format('yyyy-MM-DD') + " 23:59:59"
                            }
                            if( payway && payway !=0 ){
                                query.payway = payway
                            }
                            query ={
                                ...params,
                                ...query,
                            }
                            return await queryMyBill(query)
                        }catch(e){
                            message.error(e.message,3)
                        }
                    }
                    }
                    columns={columns}
                    manualRequest={true}
                    search={false}
                    toolBarRender={() => [
                        <Button
                        type="primary"
                        key="download"
                        onClick={() => {
                            formRef.current
                            .validateFields()
                            .then(async values => {
                                  const { rangeTime, payway } = values;
                                  let query = {}
                                  if(rangeTime && rangeTime.length>1){
                                      query.startTime = rangeTime[0].format('yyyy-MM-DD') + " 00:00:00"
                                      query.endTime = rangeTime[1].format('yyyy-MM-DD') + " 23:59:59"
                                  }
                                  if( payway && payway !=0 ){
                                      query.payway = payway
                                  }
                                  try{
                                      const rs = await downloadMyBill(query)
                                      //接收到后端的数据流以blob的方式创建一个a标签自动触发下载
                                      const blob = new Blob([rs]) //, { type: 'text/plain' })
                                      const fileName = "账单明细报表.xlsx";
                                      if ('download' in document.createElement('a')) { // 非IE下载
                                          const elink = document.createElement('a');
                                          elink.download = fileName;
                                          elink.style.display = 'none';
                                          elink.href = URL.createObjectURL(blob);
                                          document.body.appendChild(elink);
                                          elink.click();
                                          URL.revokeObjectURL(elink.href);// 释放 URL对象
                                          document.body.removeChild(elink);
                                      } else { // IE10+下载
                                          navigator.msSaveBlob(blob, fileName)
                                      }
                                  }catch(e){
                                      message.error(e.message)
                                  }
                            })
                            .catch(e => {
                                console.log(e)
                            });
                          }}
                          >
                              <DownloadOutlined />下载报表
                          </Button>
                    ]}
                />
            </Card>
            </Space>
        </PageContainer>
    )
}