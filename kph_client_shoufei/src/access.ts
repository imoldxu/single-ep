/**
 * @see https://umijs.org/zh-CN/plugins/plugin-access
 * */
export default function access(initialState: { currentUser?: API.CurrentUser | undefined }) {
  const { currentUser } = initialState || {};
  if(currentUser){
    const {roles} = currentUser;
    let admin = false;
    let tollman = false;
    roles.forEach( role =>{
      const {name} = role;
      if(name === 'manager'){
        admin = true
      }else if(name === 'tollman'){
        tollman = true
      }
    })
    return {
      admin,
      tollman,
    }
  } else{
    return {
      admin: false,
      tollman: false,
    }
  }
}
