package com.bus.chelaile.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.bus.chelaile.model.QueryParam;
import com.bus.chelaile.model.ShowType;
import com.bus.chelaile.model.ads.entity.BaseAdEntity;
import com.bus.chelaile.model.ads.entity.TaskEntity;
import com.bus.chelaile.model.ads.entity.TasksGroup;
import com.bus.chelaile.mvc.AdvParam;
import com.bus.chelaile.service.impl.DoubleAndSingleManager;
import com.bus.chelaile.service.impl.LineFeedAdsManager;
import com.bus.chelaile.service.impl.LineRightManager;
import com.bus.chelaile.service.impl.OpenManager;
import com.bus.chelaile.service.impl.StationAdsManager;
import com.bus.chelaile.util.New;

public class JSService {
//    @Autowired
//    private ServiceManager serviceManager;

    @Autowired
    private OpenManager openManager;
    @Autowired
    private StationAdsManager stationAdsManager;
    @Autowired
    private LineFeedAdsManager lineFeedAdsManager;
    @Autowired
    private DoubleAndSingleManager doubleAndSingleManager;
    @Autowired
    private LineRightManager lineRightManager;

    protected static final Logger logger = LoggerFactory.getLogger(JSService.class);

    public TaskEntity getTask(AdvParam param, String site) {
        TaskEntity taskEntity = new TaskEntity();
//        ShowType showType;
        List<BaseAdEntity> entities = null;
        switch (site) {
            case "splash":
//                showType = ShowType.OPEN_SCREEN;
                entities = openManager.doServiceList(param, ShowType.OPEN_SCREEN, new QueryParam());
                break;
            case "home":
//                showType = ShowType.DOUBLE_COLUMN;
                entities = doubleAndSingleManager.doServiceList(param, ShowType.DOUBLE_COLUMN, new QueryParam());
                break;

            case "rightTop":
//                showType = ShowType.LINE_RIGHT_ADV;
                entities = lineRightManager.doServiceList(param, ShowType.LINE_RIGHT_ADV, new QueryParam());
                break;
            case "station":
//                showType = ShowType.STATION_ADV;
                entities = stationAdsManager.doServiceList(param, ShowType.STATION_ADV, new QueryParam());
                break;

            case "bottom":
//                showType = ShowType.LINE_FEED_ADV;
                entities = lineFeedAdsManager.doServiceList(param, ShowType.LINE_FEED_ADV, new QueryParam());
                break;

            default:
                logger.error("未知类型的 site， udid={}, site={}", param);

        }
        //        List<BaseAdEntity> entities = openManager.doServiceList(param, ShowType.OPEN_SCREEN, new QueryParam());

        List<List<String>> tasks = New.arrayList();
        List<Long> times = New.arrayList();
        if (entities != null && entities.size() > 0) {
            for (BaseAdEntity entity : entities) {
                if (entity.getTasksGroup() != null) {
                    tasks.addAll(entity.getTasksGroup().getTasks());
                    times = entity.getTasksGroup().getTimeouts();
                }
            }
        }
        taskEntity.setTaskGroups(new TasksGroup(tasks, times));
        taskEntity.setTraceid(param.getUdid() + System.currentTimeMillis());

        return taskEntity;
    }
    
    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis());
    }
}
