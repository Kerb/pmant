import { motion } from "framer-motion";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Calendar, Clock, Users } from "lucide-react";
import {useEffect, useState} from "react";
import {cn} from "@/lib/utils.ts";
import {Button} from "@/components/ui/button.tsx";



export function RecentMeetings() {

  const [meetings, setMeetings] = useState([])
  const [error, setError] = useState("")

  useEffect(() => {
    setError("");
    fetch('/api/recordings', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      credentials: 'include',
      body: JSON.stringify({
        pageSize: 10,
        pageOffset: 10
      })
    })
        .then(response => response.json())
        .then(data => {
          if (data.success) {
            setMeetings(data.recordings);
            console.log('Got meetings data succesfully', data.recordings);
          } else {
            // Отображаем сообщение об ошибке от сервера или стандартное
            setError(data.message || "Invalid email or password");
            console.log('Error fetching records');
          }
        })
        .catch(error => {
          // Отображаем общее сообщение об ошибке
          setError("An error occurred during login. Please try again.");
          console.log('Error fetching records');
        });
  }, []);

  return (
      <motion.div
          initial={{y: 20, opacity: 0}}
          animate={{y: 0, opacity: 1}}
          transition={{delay: 0.4, duration: 0.4}}>

        <Button
            className="h-11 text-base font-medium mb-6"
            onClick={() => alert('Кнопка нажата!')}>
            Добавить запись
        </Button>

        <Card className="border-border shadow-md">
          <CardHeader>
            <CardTitle className="text-xl font-bold">Recent Meetings</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              {meetings && meetings.map((meeting, index) => (
                  <motion.div
                      key={meeting.id}
                      initial={{x: -20, opacity: 0}}
                      animate={{x: 0, opacity: 1}}
                      transition={{delay: 0.5 + index * 0.1, duration: 0.3}}
                      whileHover={{x: 4, transition: {duration: 0.2}}}
                      className="flex items-center justify-between p-4 rounded-2xl border border-border hover:border-primary/20 hover:bg-accent/5 transition-all duration-200 cursor-pointer"
                  >
                    <div className="flex-1 space-y-2">
                      <h3 className="font-semibold text-foreground">{meeting.title}</h3>
                      <div className="flex items-center gap-4 text-sm text-muted-foreground">
                        <div className="flex items-center gap-1">
                          <Calendar className="h-4 w-4"/>
                          <span>{meeting.date}</span>
                        </div>
                        <div className="flex items-center gap-1">
                          <Clock className="h-4 w-4"/>
                          <span>{meeting.time}</span>
                        </div>
                        <div className="flex items-center gap-1">
                          <Users className="h-4 w-4"/>
                          <span>{meeting.participants} participants</span>
                        </div>
                      </div>
                    </div>
                    <div className="flex items-center gap-3">
                      <Badge variant="secondary" className="bg-secondary text-secondary-foreground">
                        {meeting.duration}
                      </Badge>
                      <Badge className="bg-gradient-primary text-white border-0">
                        {meeting.status}
                      </Badge>
                    </div>
                  </motion.div>
              ))}
            </div>
          </CardContent>
        </Card>
      </motion.div>
  );
}
