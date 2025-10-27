import {useNavigate} from "react-router-dom";
import {useEffect, useState} from "react";
import {Card, CardDescription, CardHeader, CardTitle} from "@/components/ui/card.tsx";

const Logout = () => {
    const navigate = useNavigate();
    const [logout, setLogout] = useState("");

    useEffect(() => {
        setLogout("");
        fetch('/api/logout', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            credentials: 'include'
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    setLogout("Logout success")
                    console.log('Logout successful:', data.message);
                } else {
                    navigate("/")
                    console.log('Error during logout');
                }
            })
            .catch(error => {
                console.log('Error during logout');
            });
    }, []);

    return (
        <div className="min-h-screen bg-gradient-subtle flex items-center justify-center p-4">
            <Card className="rounded-2xl shadow-lg border-0">
                <CardHeader className="space-y-1 pb-6">
                    <CardTitle className="text-2xl font-bold text-center">
                        Bye bye 👋
                    </CardTitle>
                    {logout &&
                        <CardDescription className="text-center">
                            See you next time!
                        </CardDescription>
                    }

                </CardHeader>
            </Card>
        </div>
    );
}

export default Logout;