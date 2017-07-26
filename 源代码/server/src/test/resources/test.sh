echo ===========50 users 50 concurrent=========== >> report.txt
ab -n 50 -c 50 https://120.24.19.11:8443/v2/api/users >> report.txt
echo ===========100 users 100 concurrent=========== >> report.txt
ab -n 100 -c 100 https://120.24.19.11:8443/v2/api/users >> report.txt
echo ===========200 users 100 concurrent=========== >> report.txt
ab -n 200 -c 100 https://120.24.19.11:8443/v2/api/users >> report.txt
echo ===========200 users 200 concurrent=========== >> report.txt
ab -n 200 -c 200 https://120.24.19.11:8443/v2/api/users  >> report.txt
echo ===========500 users 100 concurrent=========== >> report.txt
ab -n 500 -c 100 https://120.24.19.11:8443/v2/api/users  >> report.txt
echo ===========500 users 200 concurrent=========== >> report.txt
ab -n 500 -c 200 https://120.24.19.11:8443/v2/api/users  >> report.txt
echo ===========1000 users 200 concurrent=========== >> report.txt
ab -n 1000 -c 200 https://120.24.19.11:8443/v2/api/users  >> report.txt
