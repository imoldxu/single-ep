export default [
  {
    path: '/user',
    layout: false,
    routes: [
      {
        path: '/user',
        routes: [
          {
            name: 'login',
            path: '/user/login',
            component: './User/login',
          },
        ],
      },
    ],
  },
  {
    name: 'order',
    icon: 'Trademark',
    path: '/OrderList',
    access: 'manager',
    component: './OrderList',
  },
  {
    path: '/dashboard',
    name: 'dashboard',
    access: 'manager',
    icon: 'dashboard',
    component: './Dashboard',
  },
  {
    name: 'prescription',
    icon: 'Snippets',
    path: '/PrescriptionList',
    access: 'manager',
    component: './PrescriptionList',
  },
  {
    name: 'print',
    hideInMenu: true,
    path: '/print',
    access: 'manager',
    component: './Print',
  },
  {
    name: 'changePassword',
    hideInMenu: true,
    access: 'admin manager tollman',
    path: '/changePassword',
    component: './ChangePassword',
  },
  {
    name: 'stock',
    icon: 'Link',
    path: '/StockList',
    access: 'manager',
    component: './StockList',
  },
  {
    name: 'bill',
    icon: 'PieChart',
    access: 'manager',
    path: '/BillList',
    component: './BillList',
  },
  {
    name: 'sale',
    icon: 'FileDone',
    access: 'manager',
    path: '/SaleList',
    component: './SaleList',
  },
  {
    name: 'charge',
    icon: 'PayCircle',
    path: '/charge',
    access: 'tollman',
    component: './Charge',
  },
  {
    name: 'saleStatistic',
    icon: 'BarChart',
    access: 'admin',
    path: '/saleStatistic',
    component: './SaleStatistic',
  },
  {
    name: 'drug',
    icon: 'Link',
    path: '/DrugList',
    access: 'admin',
    component: './DrugList',
  },
  {
    name: 'userList',
    icon: 'UsergroupAdd',
    path: '/userList',
    access: 'admin',
    component: './userList',
  },
  {
    path: '/',
    redirect: '/OrderList',
  },
  {
    component: './404',
  },
];
