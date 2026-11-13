<template>
	<el-submenu v-if="menu.list && menu.list.length >= 1" :index="menu.menuId + ''"
		:popper-class="'site-sidebar--' + sidebarLayoutSkin + '-popper'">
		<template slot="title">
			<icon-svg :name="menu.icon || ''" class="site-sidebar__menu-icon"></icon-svg>
			<span>{{ menu.name }}</span>
		</template>
		<sub-menu v-for="item in menu.list" :key="item.menuId" :menu="item" :dynamicMenuRoutes="dynamicMenuRoutes">
		</sub-menu>
	</el-submenu>
	<el-menu-item v-else :index="menu.menuId + ''" @click="gotoRouteHandle(menu)">
		<!-- <icon-svg :name="menu.icon || ''" class="site-sidebar__menu-icon"></icon-svg>
		<span>{{ menu.name }}</span> -->
		<el-badge class="item" v-if="menu.menuId==150&&chatNumRz!=0&&isAuth('authentication:pass')" :value="chatNumRz">
			<icon-svg :name="menu.icon || ''" class="site-sidebar__menu-icon"></icon-svg>
			<span>{{ menu.name }}</span>
		</el-badge>
		<el-badge class="item" v-else-if="menu.menuId==77&&chatNumFw!=0&&isAuth('autonym:tongguo')" :value="chatNumFw">
			<icon-svg :name="menu.icon || ''" class="site-sidebar__menu-icon"></icon-svg>
			<span>{{ menu.name }}</span>
		</el-badge>
		<el-badge class="item" v-else-if="menu.menuId==154&&chatNumDp!=0&&isAuth('shopManage:examine')" :value="chatNumDp">
			<icon-svg :name="menu.icon || ''" class="site-sidebar__menu-icon"></icon-svg>
			<span>{{ menu.name }}</span>
		</el-badge>
		<span v-else>
			<icon-svg :name="menu.icon || ''" class="site-sidebar__menu-icon"></icon-svg>
			<span>{{ menu.name }}</span>
		</span>
	</el-menu-item>
</template>

<script>
	import SubMenu from './main-sidebar-sub-menu'
	export default {
		name: 'sub-menu',
		data() {
			return {
				chatNumRz: 0,
				chatNumDp: 0,
				chatNumFw: 0,
			}
		},
		props: {
			menu: {
				type: Object,
				required: true
			},
			dynamicMenuRoutes: {
				type: Array,
				required: true
			}
		},
		components: {
			SubMenu
		},
		computed: {
			sidebarLayoutSkin: {
				get() {
					return this.$store.state.common.sidebarLayoutSkin
				}
			}
		},
		methods: {
			// 通过menuId与动态(菜单)路由进行匹配跳转至指定路由
			gotoRouteHandle(menu) {
				var route = this.dynamicMenuRoutes.filter(item => item.meta.menuId === menu.menuId)
				if (route.length >= 1) {
					this.$router.push({
						name: route[0].name
					})
				}
			}
		},
		mounted() {
			setInterval(() => {
				if (this.$cookie.get('numberFw')) {
					this.chatNumFw = this.$cookie.get('numberFw')
				}
				if (this.$cookie.get('numberDp')) {
					this.chatNumDp = this.$cookie.get('numberDp')
				}
				if (this.$cookie.get('numberRz')) {
					this.chatNumRz = this.$cookie.get('numberRz')
					
				}

			}, 1000)
		},
	}
</script>
<style>
	.item {
		/* margin-top: 10px;
		margin-right: 40px; */

	}

	.el-badge__content.is-fixed.is-dot {
		right: -5px;
		top: 20px;
	}
	.el-badge__content.is-fixed{
		right: 0px;
		top: 20px;
	}
</style>