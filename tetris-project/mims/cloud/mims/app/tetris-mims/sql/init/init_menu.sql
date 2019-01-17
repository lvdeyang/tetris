INSERT INTO mims_menu (id, uuid, title, link, icon, style, is_group, parent_id, menu_id_path, serial) VALUES(1, '1', '首页', '#/page-home', 'icon-home', 'font-size:17px; position:relative; top:2px;', 0, null, null, 1);
INSERT INTO mims_menu (id, uuid, title, link, icon, style, is_group, parent_id, menu_id_path, serial) VALUES(2, '2', '素材库', '#/page-material/{context:user;prop:rootFolderId}', 'icon-folder-open', 'position:relative; top:1px;', 0, null, null, 2);

#媒资库
INSERT INTO mims_menu (id, uuid, title, link, icon, style, is_group, parent_id, menu_id_path, serial) VALUES(3, '3', '媒资库', null, 'icon-picture', 'font-size:13px; margin-right:1px;', 1, null, null, 3);
INSERT INTO mims_menu (id, uuid, title, link, icon, style, is_group, parent_id, menu_id_path, serial) VALUES(4, '3-1', '图片', '#/page-media-picture', 'icon-circle-blank', 'position:relative; top:1px;', 0, 3, '/3', 1);
INSERT INTO mims_menu (id, uuid, title, link, icon, style, is_group, parent_id, menu_id_path, serial) VALUES(5, '3-2', '视频', '#/page-media-video', 'icon-circle-blank', 'position:relative; top:1px;', 0, 3, '/3', 2);
INSERT INTO mims_menu (id, uuid, title, link, icon, style, is_group, parent_id, menu_id_path, serial) VALUES(6, '3-3', '音频', '#/page-media-audio', 'icon-circle-blank', 'position:relative; top:1px;', 0, 3, '/3', 3);
INSERT INTO mims_menu (id, uuid, title, link, icon, style, is_group, parent_id, menu_id_path, serial) VALUES(7, '3-4', '视频流', '#/page-media-video-stream', 'icon-circle-blank', 'position:relative; top:1px;', 0, 3, '/3', 4);
INSERT INTO mims_menu (id, uuid, title, link, icon, style, is_group, parent_id, menu_id_path, serial) VALUES(8, '3-5', '音频流', '#/page-media-audio-stream', 'icon-circle-blank', 'position:relative; top:1px;', 0, 3, '/3', 5);
INSERT INTO mims_menu (id, uuid, title, link, icon, style, is_group, parent_id, menu_id_path, serial) VALUES(9, '3-6', '文章', '#/page-media-article', 'icon-circle-blank', 'position:relative; top:1px;', 0, 3, '/3', 6);

#任务管理
INSERT INTO mims_menu (id, uuid, title, link, icon, style, is_group, parent_id, menu_id_path, serial) VALUES(10, '4', '任务管理', null, 'icon-flag', null, 1, null, null, 4);

#审核制度
INSERT INTO mims_menu (id, uuid, title, link, icon, style, is_group, parent_id, menu_id_path, serial) VALUES(11, '5', '审核制度', null, 'icon-sitemap', 'position:relative; top:1px;', 1, null, null, 5);
INSERT INTO mims_menu (id, uuid, title, link, icon, style, is_group, parent_id, menu_id_path, serial) VALUES(12, '5-1', '图片', '#/page-check-picture', 'icon-circle-blank', 'position:relative; top:1px;', 0, 11, '/11', 1);
INSERT INTO mims_menu (id, uuid, title, link, icon, style, is_group, parent_id, menu_id_path, serial) VALUES(13, '5-2', '视频', '#/page-check-video', 'icon-circle-blank', 'position:relative; top:1px;', 0, 11, '/11', 2);
INSERT INTO mims_menu (id, uuid, title, link, icon, style, is_group, parent_id, menu_id_path, serial) VALUES(14, '5-3', '音频', '#/page-check-audio', 'icon-circle-blank', 'position:relative; top:1px;', 0, 11, '/11', 3);
INSERT INTO mims_menu (id, uuid, title, link, icon, style, is_group, parent_id, menu_id_path, serial) VALUES(15, '5-4', '视频流', '#/page-check-video-stream', 'icon-circle-blank', 'position:relative; top:1px;', 0, 11, '/11', 4);
INSERT INTO mims_menu (id, uuid, title, link, icon, style, is_group, parent_id, menu_id_path, serial) VALUES(16, '5-5', '音频流', '#/page-check-audio-stream', 'icon-circle-blank', 'position:relative; top:1px;', 0, 11, '/11', 5);
INSERT INTO mims_menu (id, uuid, title, link, icon, style, is_group, parent_id, menu_id_path, serial) VALUES(17, '5-6', '文章', '#/page-check-article', 'icon-circle-blank', 'position:relative; top:1px;', 0, 11, '/11', 6);

#用户生产流程
INSERT INTO mims_menu (id, uuid, title, link, icon, style, is_group, parent_id, menu_id_path, serial) VALUES(18, '6', '生产流程', null, 'icon-cogs', 'position:relative; top:2px;', 1, null, null, 6);
INSERT INTO mims_menu (id, uuid, title, link, icon, style, is_group, parent_id, menu_id_path, serial) VALUES(19, '6-1', '图片', '#/page-front-production-picture', 'icon-circle-blank', 'position:relative; top:1px;', 0, 18, '/18', 1);
INSERT INTO mims_menu (id, uuid, title, link, icon, style, is_group, parent_id, menu_id_path, serial) VALUES(20, '6-2', '视频', '#/page-front-production-video', 'icon-circle-blank', 'position:relative; top:1px;', 0, 18, '/18', 2);
INSERT INTO mims_menu (id, uuid, title, link, icon, style, is_group, parent_id, menu_id_path, serial) VALUES(21, '6-3', '音频', '#/page-front-production-audio', 'icon-circle-blank', 'position:relative; top:1px;', 0, 18, '/18', 3);
INSERT INTO mims_menu (id, uuid, title, link, icon, style, is_group, parent_id, menu_id_path, serial) VALUES(22, '6-4', '视频流', '#/page-front-production-video-stream', 'icon-circle-blank', 'position:relative; top:1px;', 0, 18, '/18', 4);
INSERT INTO mims_menu (id, uuid, title, link, icon, style, is_group, parent_id, menu_id_path, serial) VALUES(23, '6-5', '音频流', '#/page-front-production-audio-stream', 'icon-circle-blank', 'position:relative; top:1px;', 0, 18, '/18', 5);
INSERT INTO mims_menu (id, uuid, title, link, icon, style, is_group, parent_id, menu_id_path, serial) VALUES(24, '6-6', '文章', '#/page-front-production-article', 'icon-circle-blank', 'position:relative; top:1px;', 0, 18, '/18', 6);

#组织架构
INSERT INTO mims_menu (id, uuid, title, link, icon, style, is_group, parent_id, menu_id_path, serial) VALUES(25, '7', '组织架构', '#/page-front-organization', 'icon-group', 'position:relative; top:1px; font-size:13px; margin-right:1px;', 0, null, null, 7);

#存储管理
INSERT INTO mims_menu (id, uuid, title, link, icon, style, is_group, parent_id, menu_id_path, serial) VALUES(26, '8', '存储管理', null, 'icon-tasks', null, 0, null, null, 8);

#生产管理
INSERT INTO mims_menu (id, uuid, title, link, icon, style, is_group, parent_id, menu_id_path, serial) VALUES(27, '9', '生产管理', null, 'icon-cogs', 'position:relative; top:2px;', 1, null, null, 9);
INSERT INTO mims_menu (id, uuid, title, link, icon, style, is_group, parent_id, menu_id_path, serial) VALUES(28, '9-1', '图片', '#/page-backstage-production-picture', 'icon-circle-blank', 'position:relative; top:1px;', 0, 26, '/26', 1);
INSERT INTO mims_menu (id, uuid, title, link, icon, style, is_group, parent_id, menu_id_path, serial) VALUES(29, '9-2', '视频', '#/page-backstage-production-video', 'icon-circle-blank', 'position:relative; top:1px;', 0, 26, '/26', 2);
INSERT INTO mims_menu (id, uuid, title, link, icon, style, is_group, parent_id, menu_id_path, serial) VALUES(30, '9-3', '音频', '#/page-backstage-production-audio', 'icon-circle-blank', 'position:relative; top:1px;', 0, 26, '/26', 3);
INSERT INTO mims_menu (id, uuid, title, link, icon, style, is_group, parent_id, menu_id_path, serial) VALUES(31, '9-4', '视频流', '#/page-backstage-production-video-stream', 'icon-circle-blank', 'position:relative; top:1px;', 0, 26, '/26', 4);
INSERT INTO mims_menu (id, uuid, title, link, icon, style, is_group, parent_id, menu_id_path, serial) VALUES(32, '9-5', '音频流', '#/page-backstage-production-audio-stream', 'icon-circle-blank', 'position:relative; top:1px;', 0, 26, '/26', 5);
INSERT INTO mims_menu (id, uuid, title, link, icon, style, is_group, parent_id, menu_id_path, serial) VALUES(33, '9-6', '文章', '#/page-backstage-production-article', 'icon-circle-blank', 'position:relative; top:1px;', 0, 26, '/26', 6);


