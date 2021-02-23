/**
 * @see https://umijs.org/zh-CN/plugins/plugin-access
 * */
export default function access(initialState: { currentUser?: API.CurrentUser | undefined }) {
  const { currentUser } = initialState || {};
  if(currentUser){
    const {roles} = currentUser;
    let admin = false;
    let tollman = false;
    let manager = false;
    roles.forEach( role =>{
      const {name} = role;
      if(name === 'admin'){
        admin = true
      } else if(name === 'manager'){
        manager = true
      } else if(name === 'tollman'){
        tollman = true
      }
    })
    return {
      admin,
      manager,
      tollman,
    }
  } else{
    return {
      admin: false,
      manager: false,
      tollman: false,
    }
  }
}
