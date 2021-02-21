import React from 'react';
import { GithubOutlined } from '@ant-design/icons';
import { DefaultFooter } from '@ant-design/pro-layout';

export default () => (
    <DefaultFooter
        copyright={`${new Date().getFullYear()} 便民药房 All Rights Reserved`}
        links={[
          {
            key: 'x',
            title: '便民药房',
            href: 'http://x',
            blankTarget: true,
          },
        ]}
    />
);
