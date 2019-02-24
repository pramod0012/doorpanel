package de.tu_darmstadt.foxtrot.foxtrot_doorpanel_app;

import android.app.Application;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import de.tu_darmstadt.foxtrot.foxtrot_doorpanel_app.model.Message;
import de.tu_darmstadt.foxtrot.foxtrot_doorpanel_app.network.RetrofitClient;
import de.tu_darmstadt.foxtrot.foxtrot_doorpanel_app.network.interfacesApi.WorkerAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TabletApplication extends Application {

    private List<Worker> workerList = new ArrayList<Worker>();
    private List<Message> messageList = new ArrayList<>();

    final String UPDATE_GUI_FILTER = "de.tu_darmstadt.foxtrot.foxtrot_doorpanel_app.updateGUI";
    final String UPD_CHAT_FILTER = "de.tu_darmstadt.foxtrot.foxtrot_doorpanel_app.updateChat";

    public String room = "80b";

    public String getRoom() {
        return room;
    }

    public Worker getWorker(int position) {
        return workerList.get(position);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        pullMessages();
        pullWorkers();
    }

    public Worker getWorkerByID(int id){
        for (Worker worker : workerList){
            if (worker.getId()==id){
                return worker;
            }
        }
        return null;
    }

    public int getWorkerNum(){
        //TODO: When the server is off, the code below causes error
        return workerList.size();
    }

    public void updateWorker(int id, String key, Object value){
        Worker worker = null;
        for (Worker candidate : workerList){
            if (candidate.getId() == id){
                worker = candidate;
            }
        }

        if (key.equals("status")) {
            if (worker != null) {
                worker.setStatus((String) value);
                Intent intent = new Intent(UPDATE_GUI_FILTER);
                sendBroadcast(intent);
            }
        }
    }

    public void excludeWorker(int id) {
        for (Worker candidate: workerList) {
            if (candidate.getId() == id) {
                workerList.remove(candidate);
                break;
            }
        }
        Intent intent = new Intent(UPDATE_GUI_FILTER);
        sendBroadcast(intent);
    }

    public List<Message> getMessages() {
        return messageList;
    }

    public void addMessage(Message message) {
        messageList.add(0, message);
        Intent intent = new Intent(UPD_CHAT_FILTER);
        sendBroadcast(intent);
    }

    public void pullMessages() {
        messageList = new ArrayList<Message>() {{
            add(new Message("I am busy, sorry", "01.02.2019", "10:00:00",
                    "Andreas Brown", "visitor brad"));
            add(new Message("I am busy, sorry", "01.02.2019", "10:00:00",
                    "Andreas Brown", "visitor brad"));
            add(new Message("I am busy, sorry", "01.02.2019", "10:00:00",
                    "Andreas Brown", "visitor brad"));
            add(new Message("I am busy, sorry", "01.02.2019", "10:00:00",
                    "Andreas Brown", "visitor brad"));
        }};
    }

    public void pullWorkers(){
        WorkerAPI workerAPI = RetrofitClient.getRetrofitInstance().create(WorkerAPI.class);
        Call<List<Worker>> call= workerAPI.getAllWorkers(room);
        call.enqueue(new Callback<List<Worker>>() {
            @Override
            public void onResponse(Call<List<Worker>> call, Response<List<Worker>> response) {
                workerList = response.body();
                Intent intent = new Intent(UPDATE_GUI_FILTER);
                sendBroadcast(intent);
            }

            @Override
            public void onFailure(Call<List<Worker>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
