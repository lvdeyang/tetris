package com.suma.venus.resource.dao;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.BundlePO.SOURCE_TYPE;
import com.suma.venus.resource.pojo.FolderPO.FolderType;

@RepositoryDefinition(domainClass = BundlePO.class, idClass = Long.class)
public interface BundleDao extends CommonDao<BundlePO> {

	public BundlePO findByBundleId(String bundleId);

	public List<BundlePO> findByBundleIdIn(Collection<String> bundleIds);

	public List<BundlePO> findByDeviceModel(String deviceModel);

	public List<BundlePO> findByDeviceModelIsNotNull();

	public List<BundlePO> findByAccessNodeUid(String accessNodeUid);
	
	public int countByAccessNodeUid(String accessNodeUid);
	
	public List<BundlePO> findByAccessNodeUidIn(Collection<String> accessNodeUids);

	public List<BundlePO> findByFolderId(Long folderId);

	public List<BundlePO> findByDeviceIp(String deviceIp);

	public List<BundlePO> findBySourceType(SOURCE_TYPE sourceType);

	public List<BundlePO> findByBundleTypeAndExtraBindId(String bundleType, String extraBindId);

	public BundlePO findByUsername(String username);
	
	public List<BundlePO> findByUsernameIn(Collection<String> usernames);
	
	public BundlePO findByUserIdAndDeviceModel(Long userId, String deviceModel);
	
	public List<BundlePO> findByUserIdInAndDeviceModel(Collection<Long> userIds, String deviceModel);

	@Query("select bundle from BundlePO bundle where bundle.bundleId in ?1")
	public List<BundlePO> findInBundleIds(List<String> bundleIds);

	@Query("select bundle from BundlePO bundle where bundle.bundleType=?1 and bundle.extraBindId is null")
	public List<BundlePO> findByBundleTypeAndExtraBindIdIsNull(String bundleType);

	@Query("select bundle from BundlePO bundle where bundle.folderId is null and (bundle.bundleAlias!='播放器' or bundle.bundleAlias is null)")
	public List<BundlePO> findBundlesNoFolder();

	@Query("select bundle from BundlePO bundle where bundle.deviceModel=?1 and bundle.folderId is null and (bundle.bundleAlias!='播放器' or bundle.bundleAlias is null)")
	public List<BundlePO> findByDeviceModelAndNoFolder(String deviceModel);

	@Query("select bundle from BundlePO bundle where (bundle.bundleName like %?1% or bundle.username like %?1%) and bundle.folderId is null and (bundle.bundleAlias!='播放器' or bundle.bundleAlias is null)")
	public List<BundlePO> findBykeywordAndNoFolder(String keyword);

	@Query("select bundle from BundlePO bundle where bundle.deviceModel=?1 and (bundle.bundleName like %?2% or bundle.username like %?2%) and bundle.folderId is null and (bundle.bundleAlias!='播放器' or bundle.bundleAlias is null)")
	public List<BundlePO> findByDeviceModelAndKeywordAndNoFolder(String deviceModel, String keyword);

	@Query("select distinct bundle.bundleId from BundlePO bundle where 1=1")
	public Set<String> queryAllBundleId();

	@Query("select distinct bundle.bundleId from BundlePO bundle where bundle.deviceModel=?1")
	public Set<String> queryBundleIdByDeviceModel(String deviceModel);

	@Query("select distinct bundle.bundleId from BundlePO bundle where bundle.deviceIp like %?1%")
	public Set<String> queryBundleIdByDeviceIpLike(String deviceIp);

	@Query("select distinct bundle.bundleId from BundlePO bundle where bundle.bundleName like %?1% or bundle.username like %?1%")
	public Set<String> queryBundleIdByBundleNameOrUsernameLike(String name);

	@Query("select distinct bundle.bundleId from BundlePO bundle where bundle.deviceModel=?1 and (bundle.bundleName like %?2% or bundle.username like %?2%)")
	public Set<String> queryBundleIdByDeviceModelAndNameLike(String deviceModel, String name);

	@Query("select distinct bundle.bundleId from BundlePO bundle where bundle.accessNodeUid=?1")
	public Set<String> queryBundleIdByAccessNodeUid(String accessNodeUid);

	@Query("select distinct b.bundleId from BundlePO b where b.bundleAlias='播放器'")
	public List<String> findAllPlayerBundleIds();

	@Query("select distinct b.bundleId from BundlePO b where b.folderId=?1")
	public List<String> findBundleIdsByFolderId(Long folderId);

	@Query("select distinct b.bundleId from BundlePO b where b.folderId in (select f.id from FolderPO f where f.folderType=?1)")
	public List<String> findBundleIdsByFolderType(FolderType folderType);

	@Query("select distinct b.bundleId from BundlePO b where b.sourceType=?1")
	public Set<String> queryBundleIdsBySourceType(SOURCE_TYPE sourceType);

	@Query("select distinct b.bundleId from BundlePO b where b.deviceModel=?1 and b.sourceType=?2")
	public Set<String> queryBundleIdByDeviceModelAndSourceType(String deviceModel, SOURCE_TYPE sourceType);

	@Query("select distinct b.bundleId from BundlePO b where (b.bundleName like %?1% or b.username like %?1%) and b.sourceType=?2")
	public Set<String> queryBundleIdByNameLikeAndSourceType(String name, SOURCE_TYPE sourceType);

	@Query("select distinct b.bundleId from BundlePO b where b.deviceModel=?1 and (b.bundleName like %?2% or b.username like %?2%) and b.sourceType=?3")
	public Set<String> queryBundleIdByDeviceModelAndNameLikeAndSourceType(String deviceModel, String name,
			SOURCE_TYPE sourceType);

	/** 查询状态为未同步 需要向ldap服务器同步的设备信息 **/
	@Query("select b from BundlePO b where b.deviceModel='jv210' and (b.bundleAlias!='播放器' or b.bundleAlias is null) "
			+ "and (b.syncStatus='ASYNC' or b.syncStatus is null) and (b.sourceType='SYSTEM' or b.sourceType is null) and b.folderId is not null")
	public List<BundlePO> findBundlesSyncToLdap();

	/** 查询全部可以向ldap服务器同步的设备信息 **/
	@Query("select b from BundlePO b where b.deviceModel='jv210' and (b.bundleAlias!='播放器' or b.bundleAlias is null) "
			+ "and (b.sourceType='SYSTEM' or b.sourceType is null) and b.folderId is not null")
	public List<BundlePO> findAllBundlesSyncToLdap();

	/** 查询从ldap同步的编码器 **/
	@Query("select distinct b.bundleId from BundlePO b, ChannelSchemePO c where b.sourceType='EXTERNAL' and c.bundleId=b.bundleId and (c.channelName='VenusAudioIn' or c.channelName='VenusVideoIn')")
	public List<String> findEncoderIdsFromLdap();

	/** 查询从ldap同步的解码器 **/
	@Query("select distinct b.bundleId from BundlePO b, ChannelSchemePO c where b.sourceType='EXTERNAL' and c.bundleId=b.bundleId and (c.channelName='VenusAudioOut' or c.channelName='VenusVideoOut')")
	public List<String> findDecoderIdsFromLdap();

	/** 查询从ldap同步下来的设备ID **/
	@Query("select distinct b.bundleId from BundlePO b where b.sourceType='EXTERNAL'")
	public List<String> findBundleIdsFromLdap();

	/** 查询从bvc系统内的编码器 **/
	@Query("select distinct b.bundleId from BundlePO b, ChannelSchemePO c where b.sourceType='SYSTEM' and c.bundleId=b.bundleId and (c.channelName='VenusAudioIn' or c.channelName='VenusVideoIn')")
	public List<String> findEncoderIdsLocal();

	/** 查询bvc系统内的解码器 **/
	@Query("select distinct b.bundleId from BundlePO b, ChannelSchemePO c where b.sourceType='SYSTEM' and c.bundleId=b.bundleId and (c.channelName='VenusAudioOut' or c.channelName='VenusVideoOut')")
	public List<String> findDecoderIdsLocal();

	@Query("select b from BundlePO b where b.deviceModel='jv210' and (b.bundleAlias!='播放器' or b.bundleAlias is null) and (b.sourceType='SYSTEM' or b.sourceType is null) and b.syncStatus='SYNC'")
	public List<BundlePO> findSyncedLocalDevs();

	@Query("select b from BundlePO b where b.deviceModel='jv210' and (b.bundleAlias!='播放器' or b.bundleAlias is null) and (b.sourceType='SYSTEM' or b.sourceType is null)")
	public List<BundlePO> findLocalDevsForCleanUpLdap();

	@Query("select b from BundlePO b where (b.bundleAlias!='播放器' or b.bundleAlias is null) and (b.sourceType='SYSTEM' or b.sourceType is null)")
	public List<BundlePO> findLocalDevs();

	@Query(value = "SELECT DISTINCT bundle.* FROM bundlepo bundle LEFT JOIN channel_schemepo channel ON bundle.bundle_id = channel.bundle_id WHERE bundle.bundle_name LIKE ?1 AND bundle.bundle_id LIKE ?2 AND bundle.user_name LIKE ?3 AND bundle.device_ip LIKE ?4 And bundle.device_model = ?5 AND channel.channel_name = ?6 \n#pageable\n", countQuery = "SELECT DISTINCT COUNT(bundle.id) FROM BUNDLEPO bundle WHERE bundle.bundle_name LIKE ?1 AND bundle.bundle_id LIKE ?2 AND bundle.user_name LIKE ?3 AND bundle.device_ip LIKE ?4 And bundle.device_model = ?5 AND channel.channel_name = ?6", nativeQuery = true)
	public Page<BundlePO> findBySearch(String bundleName, String bundleId, String bundleNo, String ip,
			String deviceModel, String channelName, Pageable page);
	
	public List<BundlePO> findByUserIdNotIn(Collection<Long> ids);
	
	public List<BundlePO> findByUserIdIn(Collection<Long> ids);
	
	/**
	 *查询经纬度范围内的对应类型设备<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月6日 下午2:15:03
	 * @param Long longitude 经度
	 * @param Long latitude 纬度
	 * @param Long raidus 半径范围(米)
	 * @param String deviceModel 设备类型
	 * @return List<BundlePO>
	 */
	@Query(value = "SELECT * FROM bundlepo where device_model in ?4 AND (6371000.393 * acos (cos ( radians(?2) ) * cos( radians( latitude ) ) * cos( radians( longitude ) - radians(?1) ) + sin ( radians(?2) ) * sin( radians( latitude ) )) ) <= ?3", nativeQuery = true)
	public List<BundlePO> findByRaidus(String longitude, String latitude, Long raidus, Collection<String> deviceModel);

	public List<BundlePO> findByDeviceModelAndUserIdIn(String type, Collection<Long> userIds);
	
	public void deleteByUserId(Long userId);
	
	public void deleteByUserIdIn(Collection<Long> userIds);
	
	public BundlePO findByDeviceModelAndUsername(String deviceModel, String username);
	
	@Query(value = "select bundleId from com.suma.venus.resource.pojo.BundlePO where bundleNum in ?1")
	public List<String> findBundleIdByBundleNumIn(Collection<String> bundleNums);
}
