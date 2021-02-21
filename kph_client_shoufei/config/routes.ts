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
    path: '/dashboard',
    name: 'dashboard',
    access: 'admin',
    icon: 'dashboard',
    component: './Dashboard',
  },
  {
    name: 'order',
    icon: 'Trademark',
    path: '/OrderList',
    access: 'admin',
    component: './OrderList',
  },
  {
    name: 'prescription',
    icon: 'Snippets',
    path: '/PrescriptionList',
    access: 'admin',
    component: './PrescriptionList',
  },
  {
    name: 'print',
    hideInMenu: true,
    path: '/print',
    access: 'admin',
    component: './Print',
  },
  {
    name: 'drug',
    icon: 'Link',
    path: '/DrugList',
    access: 'admin',
    component: './DrugList',
  },
  {
    name: 'bill',
    icon: 'PieChart',
    access: 'admin',
    path: '/BillList',
    component: './BillList',
  },
  {
    name: 'sale',
    icon: 'FileDone',
    access: 'admin',
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
    path: '/',
    redirect: '/Dashboard',
  },
  {
    component: './404',
  },
];
