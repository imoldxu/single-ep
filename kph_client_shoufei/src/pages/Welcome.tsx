import React, { useRef } from 'react';
import { PageContainer } from '@ant-design/pro-layout';
import { Card, Alert, Typography, Statistic } from 'antd';
import { useIntl, FormattedMessage } from 'umi';
import styles from './Welcome.less';
import Form from 'antd/lib/form/Form';

const layout = {
  labelCol: { span: 8 },
  wrapperCol: { span: 16 },
};
const tailLayout = {
  wrapperCol: { offset: 8, span: 16 },
};

export default (): React.ReactNode => {
  const intl = useIntl();

  return (
    <PageContainer>
      <Card>
        <Alert
          message={intl.formatMessage({
            id: 'pages.welcome.alertMessage',
            defaultMessage: '欢迎使用便民药房处方系统',
          })}
          type="success"
          showIcon
          banner
          style={{
            margin: -12,
            marginBottom: 24,
          }}
        />
        <Typography.Text
          strong
          style={{
            marginBottom: 12,
          }}
        >
          欢迎使用便民药房处方系统
        </Typography.Text>
      </Card>

    </PageContainer>
  );
};
