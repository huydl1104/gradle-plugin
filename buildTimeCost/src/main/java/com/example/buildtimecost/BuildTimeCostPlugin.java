package com.example.buildtimecost;

import org.gradle.BuildListener;
import org.gradle.BuildResult;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.execution.TaskExecutionListener;
import org.gradle.api.initialization.Settings;
import org.gradle.api.invocation.Gradle;
import org.gradle.api.tasks.TaskState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BuildTimeCostPlugin implements Plugin<Project> {

    //用来记录task执行时长信息
    Map<String,TaskExecTimeInfo>  map = new HashMap<>();
    List<String> pathList = new ArrayList<>();

    @Override
    public void apply(Project project) {
        //创建一个 Extension，配置输出结果
        final BuildTimeCostExtension timeCostExt = project.getExtensions().create("taskExecTime", BuildTimeCostExtension.class);

        project.getGradle().addListener(new TaskExecutionListener() {
            @Override
            public void beforeExecute(Task task) {//任务开始执行之前
                System.out.println("TaskExecutionListener beforeExecute name -> "+task.getName());
                TaskExecTimeInfo timeInfo = new TaskExecTimeInfo();
                timeInfo.start = System.currentTimeMillis();
                timeInfo.path = task.getPath();
                map.put(task.getPath(),timeInfo);
                pathList.add(task.getPath());
            }

            @Override
            public void afterExecute(Task task, TaskState state) {//任务开始执行之后
                System.out.println("TaskExecutionListener afterExecute name -> "+task.getName()+", state ->"+state.getSkipMessage());
                TaskExecTimeInfo timeInfo = map.get(task.getPath());
                timeInfo.end = System.currentTimeMillis();
                timeInfo.total = timeInfo.end - timeInfo.start;
            }
        });

        project.getGradle().addBuildListener(new BuildListener() {
            @Override
            public void settingsEvaluated(Settings settings) {//设置评估
                System.out.println("BuildListener settingsEvaluated name -> "+settings.getGradle().toString());
            }

            @Override
            public void projectsLoaded(Gradle gradle) {//项目加载
                System.out.println("BuildListener projectsLoaded name -> "+gradle.toString());
            }

            @Override
            public void projectsEvaluated(Gradle gradle) {//项目评估
                System.out.println("BuildListener projectsEvaluated name -> "+gradle.toString());
            }

            @Override
            public void buildFinished(BuildResult result) {//build finish
                System.out.println("BuildListener buildFinished name -> "+result.getAction());
                System.out.println("build finished, now  println all task execution ");
                //按 task 执行顺序打印出执行时长信息
                if (timeCostExt.sorted){
                    List<TaskExecTimeInfo> list = new ArrayList<>();
                    Iterator<Map.Entry<String, TaskExecTimeInfo>> iterator = map.entrySet().iterator();
                    while (iterator.hasNext()){
                        list.add(iterator.next().getValue());
                    }
                    Collections.sort(list, new Comparator<TaskExecTimeInfo>() {
                        @Override
                        public int compare(TaskExecTimeInfo t2, TaskExecTimeInfo t1) {
                            return (int)(t2.total - t1.total);
                        }
                    });
                    for (TaskExecTimeInfo timeInfo : list) {
                        long t = timeInfo.total;
                        if (t >= timeCostExt.threshold) {
                            System.out.println("timeInfo.path ->"+timeInfo.path+" , t ->"+t+"ms");
                        }
                    }
                }else{
                    //按 task 执行顺序打印出执行时长信息
                    for (String path : pathList) {
                        long t = map.get(path).total;
                        if (t >= timeCostExt.threshold) {
                            System.out.println("path ->"+path +", t ->"+t+"ms");
                        }
                    }
                }
                System.out.println("---------------------------------------");

            }
        });
    }

}