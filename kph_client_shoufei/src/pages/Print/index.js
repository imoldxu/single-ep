import React, { useEffect, useRef, useState } from 'react'
import { getPrescriptionById } from '@/services/ant-design-pro/prescription';
import { useIntl, Link, history, FormattedMessage, SelectLang, useModel } from 'umi';
import styles from './index.less'
import QRCode from 'qrcode.react/lib/index'
import { Button, message, Spin } from 'antd';
import { PageContainer } from '@ant-design/pro-layout';
import { regFenToYuan } from '@/utils/money';
import { PrinterOutlined, RollbackOutlined } from '@ant-design/icons';
import ReactToPrint, { PrintContextConsumer } from 'react-to-print';

export default ()=>{

    const [detail, setDetail ] = useState();
    const [isloading, setLoading] = useState(true)

    const printRef = useRef()

    const { query } = history.location;
    const { id } = query;

    useEffect(async ()=>{
        try{
            const resp = await getPrescriptionById({id:id})
            const {code} = resp
            if(code == 1){
                setDetail(resp.data)
            }else{
                message.error(resp.message,3)
            }
        }catch(e){
            message.error(e.message,3)
        } finally{
            setLoading(false)
        }
    },[id])

    // function handlePrint(){
    //     if(document.execCommand("print") == true){
    //         message.success("打印成功")
    //     } else {
    //         message.error("打印机故障")
    //     }
    // }

    /**
     * 编码 base64 -> URL Safe base64
     * description: base64
     * '+' -> '-'
     * '/' -> '_'
     * '=' -> ''
     * param {type} string
     * return: URL Safe base64 string;
     */
    function urlSateBase64Encode(base64Str) {
        if (!base64Str) 
            return;
        let safeStr = base64Str.replace(/\+/g, '-').replace(/\//g, '_').replace(/\=/g, '’');
        return safeStr;
    }

    let price = 0
    let payCode = 0
    if(detail){
        detail.drugList.forEach(drug=>{
            price  +=  drug.price*drug.number;
        });
        let paycodeStr = JSON.stringify({"PatientName": detail.prescription.patientname,"PatientNo": detail.prescription.regNo})

        payCode = urlSateBase64Encode(window.btoa(window.encodeURIComponent(paycodeStr)))
    }


    return (
        <PageContainer 
            extra={[
                <Button type="default" key="2" onClick={()=> history.goBack()}><RollbackOutlined /> 返回</Button>,
                <ReactToPrint content={() => printRef.current}>
                <PrintContextConsumer>
                    {({ handlePrint }) => (
                        <Button type="primary" onClick={handlePrint}><PrinterOutlined /> 打印处方</Button>
                        )}
                    </PrintContextConsumer>
                </ReactToPrint>
            ]}>
            
        <Spin spinning={isloading}>
           { detail &&
            <div ref={printRef} className={`${styles.con}`}>
                <div className={styles.topHead}>四川锦欣妇女儿童医院●成都市锦江区妇幼保健金卡医院</div>
                <div className={styles.title_area}>
                    <div className={styles.title}>处方笺</div>
                    <div className={styles.tip}>普通</div>
                    <div className={styles.qrtip}>登记号</div>
                    <div className={styles.qrcode}>
                        <QRCode value={detail.prescription.regNo} level="Q" size={40}></QRCode>
                    </div>
                </div>
                
                <div className={styles.conHead}>

                    <div className={styles.row}>
                        <div className={styles.label}>姓名:</div><div className={styles.valueInput} style={{width: "100px"}}>{detail.prescription.patientname}</div>
                        <div className={styles.label}>性别:</div><div className={styles.valueInput}>{detail.prescription.patientsex}</div>
                        <div className={styles.label}>年龄:</div><div className={styles.valueInput}>{detail.prescription.patientage}</div>
                        <div className={styles.label}>费别:</div><div className={styles.valueInput}>自费</div>
                    </div>
                    <div className={styles.row}>
                        <div className={styles.label}>出生日期:</div><div className={styles.valueInput} style={{minWidth: "100px"}}>{detail.prescription.patientBirthday}</div>
                    </div>
                    <div className={styles.row}>
                        <div className={styles.label}>门诊/住院病案号：</div><div className={styles.valueInput} style={{width:"100px", marginRight: "15px"}}>{detail.prescription.regNo}</div>
                        <div className={styles.label}>科别：</div><div className={styles.valueInput} style={{width: "80px"}}>{detail.prescription.department}</div>
                    </div>
                    <div className={styles.row}>
                        <div className={styles.label}>处方号：</div><div className={styles.valueInput} style={{marginRight: "30px",padding: "0 30px"}}>{detail.prescription.prescriptionno}</div>
                        <div className={styles.label}>开具日期:</div><div className={styles.valueInput} style={{padding: "0 55px 0 20px"}}>{detail.prescription.createdate}</div>
                    </div>
                    <div className={styles.row}>
                        <div className={styles.label}>临床诊断:</div><div className={styles.valueInput} style={{width: "390px"}}>{detail.prescription.diagnosis}</div>
                    </div>

                </div>

                <div className={styles.conInfo}>

                    <div className={styles.r}>R.</div>

                    {detail.drugList.map((drug, index)=>{
                        return(
                        <div className={styles.infoList}>
                            <div style={{display: "inline-block", marginRight: "20px"}}>{index+1}</div>
                            <div style={{display: "inline-block"}}>{drug.drugname}</div>
                            <div style={{display: "inline-block"}}>{'('+drug.standard+') '+' X ' +drug.number + drug.unit}</div>
                            <div style={{marginLeft: "35px"}}><span>{'用法用量:'+ drug.singledose}</span><span style={{marginLeft: "20px"}}>{drug.frequency}</span><span style={{marginLeft: "20px"}}>{drug.myusage}</span></div>
                        </div>
                        )
                    })
                    }
                    <div style={{textAlign: "center"}}>------------------------(处方正文请勿写过此线)------------------------</div>
                </div>

                <div className={styles.conFoot}>

                    <div className={styles.row}>

                        <div className={styles.label}>医师签名并盖章:</div><div className={styles.valueInput} style={{width: "100px", marginRight: "30px"}}>{detail.prescription.doctorname}</div>
                        <div className={styles.label}>金额(元):</div><div className={styles.valueInput} style={{width: "120px"}}>{ regFenToYuan(price)}元</div>
                    </div>
                    <div className={styles.row}>  
                        <div className={styles.label} style={{letterSpacing: "2px"}}>审核'校对'发药:</div><div className={styles.valueInput} style={{width: "100px", marginRight: "20px"}}></div>
                        <div className={styles.label} style={{letterSpacing: "10px"}}>调配:</div><div className={styles.valueInput} style={{width: "120px"}}></div>
                    </div>
                    <div style={{margin: "5 0px"}}>处方用量超过七天’请医生再次确认签字。</div>
                    <div>药品属特殊商品’一经发出概不退换。</div>
                </div>

                <div className={styles.payQrCode}>
                    <QRCode value={`http://xxxx/pharmacy/scan/index?u=jxfyhpk&d=${payCode}&fairtype=f`} level="Q" size={100}></QRCode>
                </div>
            </div>
            }
        </Spin>
        </PageContainer>
    )
}